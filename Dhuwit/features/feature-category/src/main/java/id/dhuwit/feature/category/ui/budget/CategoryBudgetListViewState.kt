package id.dhuwit.feature.category.ui.budget

import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategorySearch
import id.dhuwit.state.ViewState

sealed class CategoryBudgetListViewState : ViewState.Feature() {
    data class GetCategories(val categories: List<Category>?) : CategoryBudgetListViewState()
    data class SearchCategory(val searchCategory: CategorySearch) : CategoryBudgetListViewState()
}
