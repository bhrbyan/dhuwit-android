package id.dhuwit.core.category.model

import id.dhuwit.core.category.database.CategoryEntity

data class Category(
    val name: String,
    val type: CategoryType,
    val id: Long = 0
) {
    fun toEntity(): CategoryEntity {
        return CategoryEntity(id, name, type.toString())
    }
}