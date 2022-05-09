package id.dhuwit.feature.budget.ui.form.setting

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPeriodType
import id.dhuwit.core.budget.repository.BudgetDataSource
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetFormSettingViewModel @Inject constructor(
    private val budgetRepository: BudgetDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val budgetId: Long? = savedStateHandle.get(BudgetFormSettingFragment.KEY_BUDGET_ID)

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    init {
        getBudget(budgetId)
    }

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    private fun isCreateBudget(budgetId: Long?) = budgetId == null

    private fun getBudget(budgetId: Long?) {
        if (isCreateBudget(budgetId)) {
            updateViewState(
                BudgetFormSettingViewState.GetBudget(null)
            )
        } else {
            viewModelScope.launch {
                when (val result = budgetRepository.getBudget(budgetId)) {
                    is State.Success -> {
                        updateViewState(BudgetFormSettingViewState.GetBudget(result.data))
                    }
                    is State.Error -> {
                        updateViewState(ViewState.Error(result.message))
                    }
                }
            }
        }
    }

    fun createBudget(name: String) {
        viewModelScope.launch {
            when (val result =
                budgetRepository.saveBudget(
                    Budget(
                        DEFAULT_BUDGET_ID,
                        name,
                        BudgetPeriodType.Monthly,
                        DEFAULT_BUDGET_PERIOD_DATE
                    )
                )) {
                is State.Success -> {
                    updateViewState(BudgetFormSettingViewState.CreateBudget)
                }
                is State.Error -> {
                    updateViewState(
                        ViewState.Error(result.message)
                    )
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_BUDGET_ID: Long = 0
        private const val DEFAULT_BUDGET_PERIOD_DATE: Int = 1
    }

}