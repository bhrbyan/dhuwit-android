package id.dhuwit.core.currency.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun storeCurrency(currency: CurrencyEntity)

    @Query("SELECT * FROM currency_table")
    suspend fun getCurrency(): CurrencyEntity
}