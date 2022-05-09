package id.dhuwit.feature.budget.ui.form

import id.dhuwit.state.ViewState

sealed class BudgetFormViewState : ViewState.Feature() {

    data class SetUpViewFormBudget(val budgetId: Long?) : BudgetFormViewState()

}
