package id.dhuwit.core.account.database

import androidx.room.*

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun storeAccount(account: AccountEntity)

    @Query("SELECT * FROM account_table")
    suspend fun getAccounts(): List<AccountEntity>

    @Query("SELECT * FROM account_table WHERE id = :accountId")
    suspend fun getAccount(accountId: Long?): AccountEntity?

    @Update
    suspend fun updateAccount(account: AccountEntity?)

    @Query("UPDATE account_table SET balance = :balance WHERE id = :id")
    suspend fun updateBalance(balance: Double, id: Long)

    @Query("DELETE FROM account_table WHERE id = :id")
    suspend fun deleteAccount(id: Long?)
}