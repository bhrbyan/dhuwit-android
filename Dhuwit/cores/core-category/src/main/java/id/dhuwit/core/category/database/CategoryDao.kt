package id.dhuwit.core.category.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun storeCategory(category: CategoryEntity)

    @Query("SELECT * FROM CATEGORY_TABLE WHERE type = :type")
    suspend fun getCategories(type: String): List<CategoryEntity>
}