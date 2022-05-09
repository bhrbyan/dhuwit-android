package id.dhuwit.feature.budget.ui.form.plan

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.repository.BudgetDataSource
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetFormPlanViewModel @Inject constructor(
    private val budgetRepository: BudgetDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val budgetId: Long? = savedStateHandle.get(BudgetFormPlanFragment.KEY_BUDGET_ID)

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    init {
        getBudgetPlan()
    }

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    private fun isCreatePlan(): Boolean = budgetId == null

    private fun getBudgetPlan() {
        if (isCreatePlan()) {
            updateViewState(
                BudgetFormPlanViewState.SetUpViewPlans(null)
            )
        } else {
            viewModelScope.launch {
                when (val result = budgetRepository.getBudgetPlans(budgetId, "")) {
                    is State.Success -> {
                        updateViewState(
                            BudgetFormPlanViewState.SetUpViewPlans(result.data)
                        )
                    }
                    is State.Error -> {
                        updateViewState(ViewState.Error(result.message))
                    }
                }
            }
        }
    }

}