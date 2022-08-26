package id.dhuwit.core.category.repository

import id.dhuwit.core.base.state.State
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType

interface CategoryDataSource {
    suspend fun storeCategories(categories: List<Category>): State<Boolean>
    suspend fun getCategories(type: CategoryType): State<List<Category>>
    suspend fun addCategory(category: Category): State<Category>
}