package id.dhuwit.feature.budget.ui.form

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.state.ViewState

sealed class BudgetFormViewState : ViewState.Feature() {

    data class GetBudget(val budget: Budget?) : BudgetFormViewState()

    data class UpdatePlan(val budgetPlanType: BudgetPlanType, val budgetPlans: List<BudgetPlan>?) :
        BudgetFormViewState()

}
