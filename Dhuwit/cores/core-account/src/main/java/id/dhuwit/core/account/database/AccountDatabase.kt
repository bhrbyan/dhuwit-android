package id.dhuwit.core.account.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.dhuwit.core.account.database.AccountMigration.MIGRATION_1_2

@Database(
    entities = [AccountEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AccountDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    companion object {

        private const val DATABASE_NAME: String = "account.db"

        @Volatile
        private var INSTANCE: AccountDatabase? = null

        fun getInstance(context: Context): AccountDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AccountDatabase::class.java, DATABASE_NAME
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