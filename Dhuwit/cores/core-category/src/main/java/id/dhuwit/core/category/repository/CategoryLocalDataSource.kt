package id.dhuwit.core.category.repository

import id.dhuwit.core.base.state.State
import id.dhuwit.core.category.database.CategoryDao
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(private val dao: CategoryDao) :
    CategoryDataSource {

    override suspend fun storeCategories(categories: List<Category>): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                categories.forEach { dao.storeCategory(it.toEntity()) }
                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun getCategories(type: CategoryType): State<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                val categories = dao.getCategories(type.toString()).map { it.toModel() }
                State.Success(categories)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun addCategory(category: Category): State<Category> {
        return withContext(Dispatchers.IO) {
            try {
                dao.storeCategory(category.toEntity())

                val addedCategory = dao.getCategory().toModel()
                State.Success(addedCategory)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }
}