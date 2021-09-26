package id.dhuwit.core.account.repository

import id.dhuwit.core.account.database.AccountDao
import id.dhuwit.core.account.model.Account
import id.dhuwit.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
}