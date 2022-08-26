package id.dhuwit.core.account.repository

import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.state.State
import javax.inject.Inject

class AccountRepository @Inject constructor(private val local: AccountDataSource) :
    AccountDataSource {
    override suspend fun createAccount(account: Account): State<Boolean> {
        return local.createAccount(account)
    }

    override suspend fun getAccounts(): State<List<Account>> {
        return local.getAccounts()
    }

    override suspend fun getAccount(id: Long): State<Account> {
        return local.getAccount(id)
    }

    override suspend fun updateAccount(account: Account?): State<Boolean> {
        return local.updateAccount(account)
    }

    override suspend fun deleteAccount(id: Long?): State<Boolean> {
        return local.deleteAccount(id)
    }

}