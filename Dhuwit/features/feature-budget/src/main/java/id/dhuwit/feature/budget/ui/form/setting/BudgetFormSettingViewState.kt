package id.dhuwit.feature.budget.ui.form.setting

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.state.ViewState

sealed class BudgetFormSettingViewState : ViewState.Feature() {

    object CreateBudget : BudgetFormSettingViewState()

    data class GetBudget(val budget: Budget?) : BudgetFormSettingViewState()

}
