package id.dhuwit.feature.category

import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategorySearch
import id.dhuwit.state.ViewState

sealed class CategoryListViewState : ViewState.Feature() {
    data class GetCategories(val categories: List<Category>?) : CategoryListViewState()
    data class AddCategory(val category: Category?) : CategoryListViewState()
    data class SearchCategory(val searchCategory: CategorySearch) : CategoryListViewState()
}
