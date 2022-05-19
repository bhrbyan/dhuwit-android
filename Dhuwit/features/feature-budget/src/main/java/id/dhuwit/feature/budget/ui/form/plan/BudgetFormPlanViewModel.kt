package id.dhuwit.feature.budget.ui.form.plan

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlanType
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

    private val budgetPlanType: BudgetPlanType =
        BudgetPlanType.getBudgetPlanType(savedStateHandle.get(BudgetFormPlanFragment.KEY_BUDGET_PLAN_TYPE))

    private var budget: Budget? = null

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    init {
        getBudget()
    }

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }


    private fun getBudget() {
        viewModelScope.launch {
            when (val result = budgetRepository.getBudgets()) {
                is State.Success -> {
                    if (result.data?.isNullOrEmpty() == true) {
                        updateViewState(ViewState.Error(result.message))
                    } else {
                        budget = result.data?.first()

                        getLatestBudgetPlans()
                    }
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun getLatestBudgetPlans() {
        viewModelScope.launch {
            when (val result = budgetRepository.getBudgetPlans(budget?.id)) {
                is State.Success -> {
                    updateViewState(
                        BudgetFormPlanViewState.SetUpViewPlans(result.data, budgetPlanType)
                    )
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun onAddBudgetPlan() {
        updateViewState(
            BudgetFormPlanViewState.AddBudgetPlan(budget?.id, budgetPlanType)
        )
    }

}