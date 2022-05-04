package id.dhuwit.feature.budget.ui.plan

import id.dhuwit.core.budget.model.BudgetPlanItem
import id.dhuwit.state.ViewState

sealed class BudgetPlanViewState : ViewState.Feature() {
    data class GetBudgetPlans(val plans: List<BudgetPlanItem>?) : BudgetPlanViewState()
    data class UpdateAmount(val categoryId: Long?, val plans: List<BudgetPlanItem>) :
        BudgetPlanViewState()
}
