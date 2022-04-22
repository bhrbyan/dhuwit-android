package id.dhuwit.core.transaction.repository

import id.dhuwit.core.account.database.AccountDao
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.transaction.database.TransactionDao
import id.dhuwit.core.transaction.database.TransactionEntity
import id.dhuwit.core.transaction.model.*
import id.dhuwit.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

class TransactionLocalDataSource @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
) : TransactionDataSource {

    override suspend fun getTransactions(
        transactionGetType: TransactionGetType,
        periodDate: String?
    ): State<List<Transaction>> {
        return withContext(Dispatchers.IO) {
            try {
                val transactions = when (transactionGetType) {
                    is TransactionGetType.GetAll -> transactionDao.getTransactions()
                    is TransactionGetType.GetByTransactionType -> transactionDao.getTransactionsByTransactionType(
                        transactionGetType.transactionType.toString()
                    )
                    is TransactionGetType.GetByCategoryId -> transactionDao.getTransactionsByCategoryId(
                        transactionGetType.categoryId
                    )
                }

                val filteredTransaction = if (periodDate != null) {
                    transactions.filter { transaction ->
                        isTransactionWithinPeriodDate(
                            transaction,
                            periodDate
                        )
                    }
                } else {
                    transactions
                }.sortedByDescending { transaction -> transaction.date }

                State.Success(filteredTransaction.map { it.toModel() })
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getTransaction(transactionId: Long): State<Transaction> {
        return withContext(Dispatchers.IO) {
            try {
                val transactions = transactionDao.getTransaction(transactionId).toModel()

                State.Success(transactions)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun saveTransaction(transaction: Transaction): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                // Save transaction
                transactionDao.saveTransaction(transaction.toEntity())

                // Get current account balance
                val account = accountDao.getAccount(transaction.accountId)?.toModel()

                if (account != null) {
                    // Calculate new balance
                    val newBalance = if (transaction.type is TransactionType.Expense) {
                        account.balance - transaction.amount
                    } else {
                        account.balance + transaction.amount
                    }

                    // Update account balance with new balance
                    accountDao.updateBalance(newBalance, transaction.accountId)

                    State.Success(true)
                } else {
                    State.Error("Account not found!")
                }
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun updateTransaction(
        transaction: Transaction,
        existingTransaction: Transaction?
    ): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                // Save transaction
                transactionDao.updateTransaction(transaction.toEntity())

                // Balance calculation
                if (isTransferTransaction(existingTransaction?.accountId, transaction.accountId)) {
                    val sourceAccount =
                        accountDao.getAccount(existingTransaction?.accountId)?.toModel()
                    val destinationAccount = accountDao.getAccount(transaction.accountId)?.toModel()

                    if (sourceAccount != null && destinationAccount != null) {
                        if (isChangeTransactionType(existingTransaction?.type, transaction.type)) {
                            val newBalanceSourceAccount =
                                if (existingTransaction?.type is TransactionType.Expense) {
                                    sourceAccount.balance + existingTransaction.amount
                                } else {
                                    existingTransaction?.let {
                                        sourceAccount.balance - it.amount
                                    } ?: 0.0
                                }

                            val newBalanceDestinationAccount =
                                if (transaction.type is TransactionType.Expense) {
                                    destinationAccount.balance - transaction.amount
                                } else {
                                    destinationAccount.balance + transaction.amount
                                }


                            // Update account balance with new balance
                            accountDao.updateBalance(
                                newBalanceDestinationAccount,
                                transaction.accountId
                            )
                            accountDao.updateBalance(
                                newBalanceSourceAccount,
                                existingTransaction?.accountId
                            )
                            State.Success(true)
                        } else {
                            val newBalanceSourceAccount =
                                if (existingTransaction?.type is TransactionType.Expense) {
                                    sourceAccount.balance + existingTransaction.amount
                                } else {
                                    existingTransaction?.let {
                                        sourceAccount.balance - it.amount
                                    } ?: 0.0
                                }

                            val newBalanceDestinationAccount =
                                if (existingTransaction?.type is TransactionType.Expense) {
                                    destinationAccount.balance - transaction.amount
                                } else {
                                    destinationAccount.balance + transaction.amount
                                }

                            // Update account balance with new balance
                            accountDao.updateBalance(
                                newBalanceDestinationAccount,
                                transaction.accountId
                            )
                            accountDao.updateBalance(
                                newBalanceSourceAccount,
                                existingTransaction?.accountId
                            )
                            State.Success(true)
                        }
                    } else {
                        State.Error("Account not found!")
                    }
                } else {
                    // Get current account balance
                    val account = accountDao.getAccount(existingTransaction?.accountId)?.toModel()
                    if (account != null) {
                        if (isChangeTransactionType(existingTransaction?.type, transaction.type)) {
                            // Calculate new balance
                            val newBalance = if (transaction.type is TransactionType.Expense) {
                                existingTransaction?.let {
                                    (account.balance - it.amount) - transaction.amount
                                } ?: 0.0
                            } else {
                                existingTransaction?.let {
                                    (account.balance + it.amount) + transaction.amount
                                } ?: 0.0
                            }

                            // Update account balance with new balance
                            accountDao.updateBalance(newBalance, existingTransaction?.accountId)
                            State.Success(true)
                        } else {
                            // Calculate new balance
                            val newBalance =
                                if (existingTransaction?.type is TransactionType.Expense) {
                                    resultBalanceExpenseTransaction(
                                        totalTransaction = transaction.amount,
                                        originalTotalTransaction = existingTransaction.amount,
                                        accountBalance = account.balance
                                    )
                                } else {
                                    resultBalanceIncomeTransaction(
                                        totalTransaction = transaction.amount,
                                        originalTotalTransaction = existingTransaction?.amount
                                            ?: 0.0,
                                        accountBalance = account.balance
                                    )
                                }

                            // Update account balance with new balance
                            accountDao.updateBalance(newBalance, existingTransaction?.accountId)
                            State.Success(true)
                        }
                    } else {
                        State.Error("Account not found!")
                    }
                }
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    private fun isTransferTransaction(sourceAccountId: Long?, destinationAccountId: Long): Boolean =
        sourceAccountId != destinationAccountId

    private fun isChangeTransactionType(
        sourceTransactionType: TransactionType?,
        destinationTransactionType: TransactionType
    ): Boolean =
        sourceTransactionType != destinationTransactionType

    private fun resultBalanceExpenseTransaction(
        totalTransaction: Double,
        originalTotalTransaction: Double,
        accountBalance: Double
    ): Double {
        val difference = originalTotalTransaction - totalTransaction
        return if (difference >= 0) {
            accountBalance + difference
        } else {
            accountBalance - abs(difference)
        }
    }

    private fun resultBalanceIncomeTransaction(
        totalTransaction: Double,
        originalTotalTransaction: Double,
        accountBalance: Double
    ): Double {
        val difference = originalTotalTransaction - totalTransaction
        return if (difference >= 0) {
            accountBalance - difference
        } else {
            accountBalance + abs(difference)
        }
    }

    override suspend fun deleteTransaction(transactionId: Long): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                // Get existing transaction
                val transaction = transactionDao.getTransaction(transactionId)

                // Get current account balance
                val account = accountDao.getAccount(transaction.accountId)?.toModel()

                if (account != null) {
                    val accountBalance = account.balance

                    // Calculate new balance
                    val newBalance =
                        if (TransactionType.getTransactionType(transaction.type) is TransactionType.Expense) {
                            accountBalance + transaction.amount
                        } else {
                            accountBalance - transaction.amount
                        }

                    // Update account balance with new balance
                    accountDao.updateBalance(newBalance, transaction.accountId)
                }

                // Delete transaction
                transactionDao.deleteTransaction(transactionId)

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getCategoryTransaction(
        periodDate: String?,
        categoryType: CategoryType
    ): State<List<TransactionCategory>> {
        return withContext(Dispatchers.IO) {
            try {
                val transactions = transactionDao.getTransactions()
                val filteredTransaction = transactions
                    .filter { transaction ->
                        isTransactionWithinPeriodDate(
                            transaction,
                            periodDate
                        ) && CategoryType.getCategoryType(transaction.categoryType) == categoryType
                    }
                val distinctTransactions = filteredTransaction.distinctBy { it.categoryId }

                val categories: ArrayList<TransactionCategory> = ArrayList()
                distinctTransactions.forEach { distinctTransaction ->
                    val totalTransaction = filteredTransaction.filter {
                        it.categoryId == distinctTransaction.categoryId
                    }.size

                    val totalAmountTransaction = filteredTransaction.filter {
                        it.categoryId == distinctTransaction.categoryId
                    }.sumOf { it.amount }

                    categories.add(
                        TransactionCategory(
                            categoryId = distinctTransaction.categoryId,
                            categoryName = distinctTransaction.categoryName,
                            totalAmountTransaction = totalAmountTransaction,
                            totalTransaction = totalTransaction
                        )
                    )
                }

                val sortedCategories = categories.sortedBy { it.categoryName }

                State.Success(sortedCategories)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    private fun isTransactionWithinPeriodDate(
        transaction: TransactionEntity,
        periodDate: String?
    ): Boolean {
        return DateHelper.isTransactionDateWithinRangePeriodDate(
            transactionDate = transaction.date.convertPattern(
                DateHelper.PATTERN_DATE_DATABASE,
                DateHelper.PATTERN_DATE_PERIOD
            ),
            periodDate = periodDate ?: ""
        )
    }

    override suspend fun getAccountTransaction(periodDate: String?): State<List<TransactionAccount>> {
        return withContext(Dispatchers.IO) {
            try {
                val transactions = transactionDao.getTransactions()
                val filteredTransaction = transactions
                    .filter { transaction ->
                        isTransactionWithinPeriodDate(
                            transaction,
                            periodDate
                        )
                    }
                val distinctTransactions = filteredTransaction.distinctBy { it.accountId }

                val accounts: ArrayList<TransactionAccount> = ArrayList()
                distinctTransactions.forEach { distinctTransaction ->
                    val totalTransaction = filteredTransaction.filter {
                        it.accountId == distinctTransaction.accountId
                    }.size

                    val totalAmountTransaction = filteredTransaction.filter {
                        it.accountId == distinctTransaction.accountId
                    }.sumOf { it.amount }

                    val accountName = accountDao.getAccount(distinctTransaction.accountId)?.name

                    accounts.add(
                        TransactionAccount(
                            accountId = distinctTransaction.accountId,
                            accountName = accountName,
                            totalAmountTransaction = totalAmountTransaction,
                            totalTransaction = totalTransaction
                        )
                    )
                }

                val sortedCategories = accounts.sortedBy { it.accountName }

                State.Success(sortedCategories)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }
}