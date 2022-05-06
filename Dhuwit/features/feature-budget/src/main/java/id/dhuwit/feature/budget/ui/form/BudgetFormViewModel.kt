package id.dhuwit.feature.budget.ui.form

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPeriodType
import id.dhuwit.core.budget.repository.BudgetDataSource
import id.dhuwit.feature.budget.ui.BudgetConstants
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetFormViewModel @Inject constructor(
    private val budgetRepository: BudgetDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val budgetId: Long? = savedStateHandle.get<Long>(BudgetConstants.KEY_BUDGET_ID)

    private var budget: Budget? = null

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        getBudget(budgetId)
    }

    private fun setBudget(budget: Budget?) {
        this.budget = budget
    }

    private fun getBudget(budgetId: Long?) {
        viewModelScope.launch {
            if (isCreateBudget()) {
                setBudget(
                    Budget(
                        id = budgetId,
                        name = "",
                        periodType = BudgetPeriodType.Monthly,
                        periodDate = DEFAULT_PERIOD_DATE
                    )
                )

                updateViewState(BudgetFormViewState.GetBudget(budget))
            } else {
                when (val result = budgetRepository.getBudget(budgetId)) {
                    is State.Success -> {
                        setBudget(result.data)
                        updateViewState(BudgetFormViewState.GetBudget(budget))
                    }
                    is State.Error -> {
                        updateViewState(ViewState.Error(result.message))
                    }
                }
            }
        }
    }

    private fun isCreateBudget(): Boolean = budgetId == null

    fun saveBudget(name: String) {
        if (name.isNotEmpty()) {
            budget?.name = name

            viewModelScope.launch {
                budget?.let {
                    when (val result = budgetRepository.saveBudget(it)) {
                        is State.Success -> {
                            updateViewState(
                                BudgetFormViewState.SaveBudget
                            )
                        }
                        is State.Error -> {
                            updateViewState(
                                ViewState.Error(result.message)
                            )
                        }
                    }
                }
            }
        } else {
            updateViewState(
                BudgetFormViewState.ShowErrorRequirement
            )
        }
    }

    companion object {
        private const val DEFAULT_PERIOD_DATE: Int = 1
    }

}