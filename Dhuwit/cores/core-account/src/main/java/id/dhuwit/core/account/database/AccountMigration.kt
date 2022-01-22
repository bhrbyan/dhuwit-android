package id.dhuwit.core.account.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AccountMigration {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE account_table ADD COLUMN is_primary INTEGER DEFAULT 1 NOT NULL"
            )
        }
    }

}