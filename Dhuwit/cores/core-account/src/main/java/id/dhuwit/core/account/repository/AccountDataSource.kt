package id.dhuwit.core.account.repository

import id.dhuwit.core.account.model.Account
import id.dhuwit.state.State

interface AccountDataSource {
    suspend fun storeAccount(account: Account): State<Boolean>
    suspend fun getAccount(): State<Account>
}