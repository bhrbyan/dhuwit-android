package id.dhuwit.feature.budget.ui.form

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.core.budget.model.BudgetSetting
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

    private val budgetId: Long = savedStateHandle.get<Long>(BudgetConstants.KEY_BUDGET_ID)
        ?: BudgetConstants.DEFAULT_BUDGET_ID

    private var budgetPlanIncomes: List<BudgetPlan>? = null
    private var budgetPlanExpenses: List<BudgetPlan>? = null

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        getBudget(budgetId)
    }

    private fun getBudget(budgetId: Long) {
        viewModelScope.launch {
            if (budgetId != BudgetConstants.DEFAULT_BUDGET_ID) {
                when (val result = budgetRepository.getBudget(budgetId)) {
                    is State.Success -> {
                        updateViewState(
                            BudgetFormViewState.GetBudget(result.data)
                        )
                    }
                    is State.Error -> {
                        updateViewState(ViewState.Error(result.message))
                    }
                }
            } else {
                updateViewState(
                    BudgetFormViewState.GetBudget(
                        Budget(
                            setting = BudgetSetting(),
                            incomes = emptyList(),
                            expenses = emptyList()
                        )
                    )
                )
            }
        }
    }

    fun updatePlan(budgetPlanType: BudgetPlanType) {
        when (budgetPlanType) {
            is BudgetPlanType.Income -> {
                budgetPlanIncomes = budgetRepository.budgetPlanIncomesTemp
                updateViewState(
                    BudgetFormViewState.UpdatePlan(budgetPlanType, budgetPlanIncomes)
                )
            }
            is BudgetPlanType.Expense -> {
                budgetPlanExpenses = budgetRepository.budgetPlanExpensesTemp
                updateViewState(
                    BudgetFormViewState.UpdatePlan(budgetPlanType, budgetPlanExpenses)
                )
            }
        }
    }

    companion object {
        private const val DEFAULT_PERIOD_DATE_POSITION: Int = 0
    }

}