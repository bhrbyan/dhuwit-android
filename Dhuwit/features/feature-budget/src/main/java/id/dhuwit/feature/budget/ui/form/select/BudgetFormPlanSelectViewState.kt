package id.dhuwit.feature.budget.ui.form.select

import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.core.category.model.Category
import id.dhuwit.state.ViewState

sealed class BudgetFormPlanSelectViewState : ViewState.Feature() {

    data class SelectCategory(val category: Category) : BudgetFormPlanSelectViewState()

    data class GetBudgetPlan(val budgetPlan: BudgetPlan?) : BudgetFormPlanSelectViewState()
    data class OpenBudgetCategoryPage(val budgetPlanType: BudgetPlanType) :
        BudgetFormPlanSelectViewState()

    object SaveBudget : BudgetFormPlanSelectViewState()
    object UpdateBudget : BudgetFormPlanSelectViewState()
    object DeleteBudget : BudgetFormPlanSelectViewState()
}
