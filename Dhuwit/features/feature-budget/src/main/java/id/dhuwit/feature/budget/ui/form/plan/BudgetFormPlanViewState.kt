package id.dhuwit.feature.budget.ui.form.plan

import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.state.ViewState

sealed class BudgetFormPlanViewState : ViewState.Feature() {

    data class SetUpViewPlans(val plans: List<BudgetPlan>?) : BudgetFormPlanViewState()

    data class AddBudgetPlan(val budgetId: Long?, val budgetPlanType: BudgetPlanType) :
        BudgetFormPlanViewState()

}
