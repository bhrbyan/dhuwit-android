package id.dhuwit.feature.budget.ui

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetData
import id.dhuwit.state.ViewState

sealed class BudgetViewState : ViewState.Feature() {

    data class GetBudget(
        val budgets: List<Budget>?,
        val budgetDataIncomes: List<BudgetData>?,
        val budgetDataExpenses: List<BudgetData>?
    ) : BudgetViewState()

    data class OpenFormBudget(val budgetId: Long?) : BudgetViewState()

}