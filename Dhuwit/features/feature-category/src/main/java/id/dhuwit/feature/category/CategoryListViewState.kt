package id.dhuwit.feature.category

import id.dhuwit.core.base.state.ViewState
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategorySearch

sealed class CategoryListViewState : ViewState.Feature() {
    data class GetCategories(val categories: List<Category>?) : CategoryListViewState()
    data class AddCategory(val category: Category?) : CategoryListViewState()
    data class SearchCategory(val searchCategory: CategorySearch) : CategoryListViewState()
}
