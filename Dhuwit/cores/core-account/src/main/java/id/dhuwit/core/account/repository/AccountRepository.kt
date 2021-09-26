package id.dhuwit.core.account.repository

import id.dhuwit.core.account.model.Account
import id.dhuwit.state.State
import javax.inject.Inject

class AccountRepository @Inject constructor(private val local: AccountDataSource) :
    AccountDataSource {
    override suspend fun storeAccount(account: Account): State<Boolean> {
        return local.storeAccount(account)
    }

    override suspend fun getAccount(): State<Account> {
        return local.getAccount()
    }

    override suspend fun updateAccount(account: Account): State<Boolean> {
        return local.updateAccount(account)
    }
}