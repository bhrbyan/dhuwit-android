package id.dhuwit.core.account.database

import androidx.room.*

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun storeAccount(account: AccountEntity)

    @Query("SELECT * FROM account_table")
    suspend fun getAccount(): AccountEntity

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Query("UPDATE account_table SET balance = :balance")
    suspend fun updateBalance(balance: Double)
}