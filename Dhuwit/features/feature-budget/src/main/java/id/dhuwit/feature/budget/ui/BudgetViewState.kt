package id.dhuwit.feature.budget.ui

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.state.ViewState

sealed class BudgetViewState : ViewState.Feature() {

    data class GetBudget(
        val budgets: List<Budget>?,
        val budgetPlanIncomes: List<BudgetPlan>?,
        val budgetPlanExpens: List<BudgetPlan>?
    ) : BudgetViewState()

    data class OpenFormBudget(val budgetId: Long?) : BudgetViewState()

}