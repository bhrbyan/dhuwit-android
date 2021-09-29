package id.dhuwit.core.account.repository

import id.dhuwit.core.account.database.AccountDao
import id.dhuwit.core.account.model.Account
import id.dhuwit.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

class AccountLocalDataSource @Inject constructor(private val dao: AccountDao) : AccountDataSource {
    override suspend fun storeAccount(account: Account): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.storeAccount(account.toEntity())

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun getAccount(): State<Account> {
        return withContext(Dispatchers.IO) {
            try {
                val account = dao.getAccount().toModel()

                State.Success(account)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun updateAccount(account: Account): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.updateAccount(account.toEntity())

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    /* Called when new transaction */
    override suspend fun updateBalance(
        totalTransaction: Double,
        isExpenseTransaction: Boolean
    ): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val accountBalance = dao.getAccount().toModel().balance

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
        totalTransaction: Double,
        originalTotalTransaction: Double,
        isExpenseTransaction: Boolean
    ): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val accountBalance = dao.getAccount().toModel().balance

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
        isExpenseTransaction: Boolean,
        totalTransaction: Double
    ): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val accountBalance = dao.getAccount().toModel().balance

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