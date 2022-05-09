package id.dhuwit.feature.budget.ui.form.select

import id.dhuwit.core.category.model.Category
import id.dhuwit.state.ViewState

sealed class BudgetFormPlanSelectViewState : ViewState.Feature() {

    data class SetSelectedCategory(val category: Category) : BudgetFormPlanSelectViewState()

    object SetUpViewAddPlan : BudgetFormPlanSelectViewState()
    object SetUpViewUpdatePlan : BudgetFormPlanSelectViewState()

}
