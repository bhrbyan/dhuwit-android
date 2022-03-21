package id.dhuwit.core.transaction.repository

import id.dhuwit.core.account.database.AccountDao
import id.dhuwit.core.transaction.database.TransactionDao
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

class TransactionLocalDataSource @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
) : TransactionDataSource {

    override suspend fun getTransactions(): State<List<Transaction>> {
        return withContext(Dispatchers.IO) {
            try {
                val transactions = transactionDao.getTransactions().map { it.toModel() }

                State.Success(transactions)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getTransaction(id: Long): State<Transaction> {
        return withContext(Dispatchers.IO) {
            try {
                val transactions = transactionDao.getTransaction(id).toModel()

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

                // Get current account balance
                val account = accountDao.getAccount(transaction.accountId)?.toModel()

                if (account != null) {
                    // Calculate new balance
                    val newBalance = if (transaction.type is TransactionType.Expense) {
                        resultBalanceExpenseTransaction(
                            totalTransaction = transaction.amount,
                            originalTotalTransaction = existingTransaction?.amount ?: 0.0,
                            accountBalance = account.balance
                        )
                    } else {
                        resultBalanceIncomeTransaction(
                            totalTransaction = transaction.amount,
                            originalTotalTransaction = existingTransaction?.amount ?: 0.0,
                            accountBalance = account.balance
                        )
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
}