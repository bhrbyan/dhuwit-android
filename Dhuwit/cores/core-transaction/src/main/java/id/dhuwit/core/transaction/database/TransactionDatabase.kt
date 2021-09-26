package id.dhuwit.core.transaction.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {

        private const val DATABASE_NAME: String = "transaction.db"

        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        fun getInstance(context: Context): TransactionDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        TransactionDatabase::class.java, DATABASE_NAME
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