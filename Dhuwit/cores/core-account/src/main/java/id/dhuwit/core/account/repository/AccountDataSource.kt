package id.dhuwit.core.account.repository

import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.state.State

interface AccountDataSource {
    suspend fun createAccount(account: Account): State<Boolean>

    suspend fun getAccounts(): State<List<Account>>

    suspend fun getAccount(id: Long): State<Account>

    suspend fun updateAccount(account: Account?): State<Boolean>

    suspend fun deleteAccount(id: Long?): State<Boolean>

}