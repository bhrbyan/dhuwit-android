package id.dhuwit.feature.budget.ui.plan

import id.dhuwit.core.category.model.Category
import id.dhuwit.state.ViewState

sealed class BudgetPlanViewState : ViewState.Feature() {

    data class SetSelectedCategory(val category: Category) : BudgetPlanViewState()

}
