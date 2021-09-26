package id.dhuwit.core.currency.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CurrencyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    companion object {

        private const val DATABASE_NAME: String = "currency.db"

        @Volatile
        private var INSTANCE: CurrencyDatabase? = null

        fun getInstance(context: Context): CurrencyDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        CurrencyDatabase::class.java, DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                        .also { INSTANCE = it }
                }
                return instance
            }
        }
    }
}