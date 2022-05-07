package id.dhuwit.feature.budget.ui.form

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.state.ViewState

sealed class BudgetFormViewState : ViewState.Feature() {

    object SaveBudget : BudgetFormViewState()
    object ShowErrorRequirement : BudgetFormViewState()

    data class GetBudget(val budget: Budget?) : BudgetFormViewState()

}
