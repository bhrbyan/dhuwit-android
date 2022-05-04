package id.dhuwit.feature.budget.ui.plan

import id.dhuwit.core.budget.model.BudgetPlanItem
import id.dhuwit.state.ViewState

sealed class BudgetPlanViewState : ViewState.Feature() {
    data class GetCategories(val plans: List<BudgetPlanItem>?) : BudgetPlanViewState()
}
