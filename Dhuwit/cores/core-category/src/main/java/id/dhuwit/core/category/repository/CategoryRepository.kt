package id.dhuwit.core.category.repository

import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.state.State
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val local: CategoryDataSource) :
    CategoryDataSource {

    override suspend fun storeCategories(categories: List<Category>): State<Boolean> {
        return local.storeCategories(categories)
    }

    override suspend fun getCategories(type: CategoryType): State<List<Category>> {
        return local.getCategories(type)
    }
}