package id.dhuwit.core.account.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun storeAccount(account: AccountEntity)

    @Query("SELECT * FROM account_table")
    suspend fun getAccount(): AccountEntity
}