package id.dhuwit.core.transaction.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object TransactionMigration {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE transaction_table ADD COLUMN account_id INTEGER DEFAULT 1 NOT NULL"
            )
        }
    }

}