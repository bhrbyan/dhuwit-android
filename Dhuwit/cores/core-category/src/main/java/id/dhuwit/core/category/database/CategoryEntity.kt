package id.dhuwit.core.category.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType

@Entity(tableName = "category_table")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "type") var type: String
) {
    fun toModel(): Category {
        return Category(name, CategoryType.getCategoryType(type), id)
    }
}
