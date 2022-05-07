package id.dhuwit.feature.budget.ui.form

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetData
import id.dhuwit.core.budget.model.BudgetDataType
import id.dhuwit.state.ViewState

sealed class BudgetFormViewState : ViewState.Feature() {

    object SaveBudget : BudgetFormViewState()
    object ShowErrorRequirement : BudgetFormViewState()

    data class GetBudget(val budget: Budget?) : BudgetFormViewState()

    data class UpdatePlan(val budgetDataType: BudgetDataType, val budgetData: List<BudgetData>?) :
        BudgetFormViewState()

}
