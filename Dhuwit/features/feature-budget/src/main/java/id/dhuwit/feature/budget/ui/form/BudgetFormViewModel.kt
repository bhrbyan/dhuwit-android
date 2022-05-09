package id.dhuwit.feature.budget.ui.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.feature.budget.ui.BudgetConstants
import id.dhuwit.state.ViewState
import javax.inject.Inject

@HiltViewModel
class BudgetFormViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val budgetId: Long? = savedStateHandle.get<Long>(BudgetConstants.KEY_BUDGET_ID)

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    init {
        getBudget()
    }

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    private fun getBudget() {
        updateViewState(BudgetFormViewState.SetUpViewFormBudget(budgetId))
    }

}