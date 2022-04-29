package id.dhuwit.feature.budget.ui

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.state.ViewState

sealed class BudgetViewState : ViewState.Feature() {

    data class GetBudget(val budget: Budget?) : BudgetViewState()

    object ShowEmptyState : BudgetViewState()
}