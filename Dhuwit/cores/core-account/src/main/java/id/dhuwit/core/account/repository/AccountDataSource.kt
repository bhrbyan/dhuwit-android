package id.dhuwit.core.account.repository

import id.dhuwit.core.account.model.Account
import id.dhuwit.state.State

interface AccountDataSource {
    suspend fun storeAccount(account: Account): State<Boolean>

    suspend fun getAccounts(): State<List<Account>>

    suspend fun getAccount(id: Long): State<Account>

    suspend fun updateAccount(account: Account): State<Boolean>

    suspend fun deleteAccount(id: Long): State<Boolean>

    suspend fun updateBalance(
        totalTransaction: Double,
        isExpenseTransaction: Boolean
    ): State<Boolean>

    suspend fun updateBalance(
        totalTransaction: Double,
        originalTotalTransaction: Double,
        isExpenseTransaction: Boolean,
    ): State<Boolean>

    suspend fun updateBalance(
        isExpenseTransaction: Boolean,
        totalTransaction: Double
    ): State<Boolean>
}