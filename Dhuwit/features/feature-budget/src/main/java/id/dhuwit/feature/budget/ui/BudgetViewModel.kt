package id.dhuwit.feature.budget.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetDataType
import id.dhuwit.core.budget.repository.BudgetDataSource
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetDataSource
) : ViewModel() {

    private var budget: Budget? = null
    private var periodMonth: Int = CURRENT_MONTH
    private var periodDate: String? = ""

    private val _viewState = MutableLiveData<ViewState?>()
    val viewState: LiveData<ViewState?> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun resetViewState() {
        _viewState.value = null
    }

    init {
        setPeriodDate(null)
        getBudgets()
    }

    fun getBudgets() {
        val date = periodDate ?: this.periodDate

        viewModelScope.launch {
            when (val result = budgetRepository.getBudgets()) {
                is State.Success -> {
                    getBudgetData(result.data, date)
                }
                is State.Error -> {
                    updateViewState(
                        ViewState.Error(result.message)
                    )
                }
            }
        }
    }

    private suspend fun getBudgetData(budgets: List<Budget>?, date: String?) {
        when (val result = budgetRepository.getBudgetData(budget?.id, date)) {
            is State.Success -> {
                val budgetDataIncomes =
                    result.data?.filter { it.budgetDataType is BudgetDataType.Income }

                val budgetDataExpenses =
                    result.data?.filter { it.budgetDataType is BudgetDataType.Expense }

                updateViewState(
                    BudgetViewState.GetBudget(budgets, budgetDataIncomes, budgetDataExpenses)
                )
            }
            is State.Error -> {
                updateViewState(
                    ViewState.Error(result.message)
                )
            }
        }
    }

    fun openFormBudget() {
        updateViewState(
            BudgetViewState.OpenFormBudget(
                budget?.id
            )
        )
    }

    fun onNextPeriodDate() {
        setPeriodDate(++periodMonth)
    }

    fun onPreviousPeriodDate() {
        setPeriodDate(--periodMonth)
    }

    fun setPeriodDate(periodDate: Int?) {
        val date = periodDate ?: this.periodMonth
        this.periodDate = DateHelper.getPeriodDate(date, DateHelper.PATTERN_DATE_PERIOD)
    }

    companion object {
        private const val CURRENT_MONTH: Int = 0
    }

}