package id.dhuwit.core.transaction.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.dhuwit.core.transaction.database.TransactionMigration.MIGRATION_1_2

@Database(
    entities = [TransactionEntity::class],
    version = 2,
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
                        .addMigrations(MIGRATION_1_2)
                        .build()
                        .also { INSTANCE = it }
                }
                return instance
            }
        }
    }
}