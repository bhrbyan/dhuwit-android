package id.dhuwit.core.account.repository

import id.dhuwit.core.account.database.AccountDao
import id.dhuwit.core.account.database.AccountEntity
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
                val account = dao.getAccount(id)?.toModel()

                if (account != null) {
                    State.Success(account)
                } else {
                    State.Error("Account not found")
                }
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun updateAccount(account: Account?): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val currentAccountData = dao.getAccount(account?.id)
                if (currentAccountData?.isPrimary != account?.isPrimary) {
                    if (account?.isPrimary == true) {
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

                dao.updateAccount(account?.toEntity())

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun deleteAccount(id: Long?): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.deleteAccount(id)

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

}