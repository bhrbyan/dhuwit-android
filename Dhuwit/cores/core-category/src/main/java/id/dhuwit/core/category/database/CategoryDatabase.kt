package id.dhuwit.core.category.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CategoryDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    companion object {

        private const val DATABASE_NAME: String = "category.db"

        @Volatile
        private var INSTANCE: CategoryDatabase? = null

        fun getInstance(context: Context): CategoryDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        CategoryDatabase::class.java, DATABASE_NAME
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