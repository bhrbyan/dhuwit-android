package id.dhuwit.core.account.repository

import id.dhuwit.core.account.database.AccountDao
import id.dhuwit.core.account.database.AccountEntity
import id.dhuwit.core.account.model.Account
import id.dhuwit.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

class AccountLocalDataSource @Inject constructor(private val dao: AccountDao) : AccountDataSource {

    override suspend fun createAccount(account: Account): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                if (account.isPrimary) {
                    val allAccountsData = dao.getAccounts()
                    if (allAccountsData.isNotEmpty()) {
                        val primaryAccountData = allAccountsData.find { it.isPrimary }
                        if (primaryAccountData != null) {
                            dao.updateAccount(
                                AccountEntity(
                                    id = primaryAccountData.id,
                                    name = primaryAccountData.name,
                                    balance = primaryAccountData.balance,
                                    isPrimary = false
                                )
                            )
                        }
                    }
                }

                dao.storeAccount(account.toEntity())

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun getAccounts(): State<List<Account>> {
        return withContext(Dispatchers.IO) {
            try {
                val accounts = dao.getAccounts().map { account ->
                    account.toModel()
                }

                State.Success(accounts)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun getAccount(id: Long): State<Account> {
        return withContext(Dispatchers.IO) {
            try {
                val account = dao.getAccount(id).toModel()

                State.Success(account)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun updateAccount(account: Account): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val currentAccountData = dao.getAccount(account.id)
                if (currentAccountData.isPrimary != account.isPrimary) {
                    if (account.isPrimary) {
                        val allAccountsData = dao.getAccounts()
                        val primaryAccountData = allAccountsData.find { it.isPrimary }
                        if (primaryAccountData != null) {
                            dao.updateAccount(
                                AccountEntity(
                                    id = primaryAccountData.id,
                                    name = primaryAccountData.name,
                                    balance = primaryAccountData.balance,
                                    isPrimary = false
                                )
                            )
                        }
                    }
                }

                dao.updateAccount(account.toEntity())

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun deleteAccount(id: Long): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.deleteAccount(id)

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    /* Called when new transaction */
    override suspend fun updateBalance(
        accountId: Long,
        totalTransaction: Double,
        isExpenseTransaction: Boolean
    ): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val accountBalance = dao.getAccount(accountId).toModel().balance

                val balance = if (isExpenseTransaction) {
                    accountBalance - totalTransaction
                } else {
                    accountBalance + totalTransaction
                }

                dao.updateBalance(balance)
                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    /* Called when update transaction */
    override suspend fun updateBalance(
        accountId: Long,
        totalTransaction: Double,
        originalTotalTransaction: Double,
        isExpenseTransaction: Boolean
    ): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val accountBalance = dao.getAccount(accountId).toModel().balance

                val balance = if (isExpenseTransaction) {
                    resultBalanceExpenseTransaction(
                        totalTransaction,
                        originalTotalTransaction,
                        accountBalance
                    )
                } else {
                    resultBalanceIncomeTransaction(
                        totalTransaction,
                        originalTotalTransaction,
                        accountBalance
                    )
                }

                dao.updateBalance(balance)
                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
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

    /* Called when delete transaction */
    override suspend fun updateBalance(
        accountId: Long,
        isExpenseTransaction: Boolean,
        totalTransaction: Double
    ): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val accountBalance = dao.getAccount(accountId).toModel().balance

                val balance = if (isExpenseTransaction) {
                    accountBalance + totalTransaction
                } else {
                    accountBalance - totalTransaction
                }

                dao.updateBalance(balance)
                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }
}