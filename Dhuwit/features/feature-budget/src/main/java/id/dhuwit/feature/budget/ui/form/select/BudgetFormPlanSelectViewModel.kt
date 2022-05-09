package id.dhuwit.feature.budget.ui.form.select

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.core.budget.repository.BudgetDataSource
import id.dhuwit.core.category.model.Category
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_SELECT_BUDGET_ID
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_SELECT_BUDGET_PLAN_ID
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_SELECT_BUDGET_PLAN_TYPE
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetFormPlanSelectViewModel @Inject constructor(
    private val budgetRepository: BudgetDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val budgetPlanId: Long? = savedStateHandle.get(KEY_SELECT_BUDGET_PLAN_ID)
    private val budgetId: Long =
        savedStateHandle.get(KEY_SELECT_BUDGET_ID) ?: throw Exception("budgetId shouldn't be null")
    private val budgetPlanType: BudgetPlanType =
        BudgetPlanType.getBudgetPlanType(savedStateHandle.get(KEY_SELECT_BUDGET_PLAN_TYPE))

    private var budgetPlan: BudgetPlan? = null
    private lateinit var selectedCategory: Category

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        getBudgetPlan()
    }

    private fun getBudgetPlan() {
        if (budgetPlanId != null) {
            viewModelScope.launch {
                when (val result = budgetRepository.getBudgetPlan(budgetPlanId)) {
                    is State.Success -> {
                        budgetPlan = result.data
                        updateViewState(BudgetFormPlanSelectViewState.GetBudgetPlan(budgetPlan))
                    }
                    is State.Error -> {
                        updateViewState(
                            ViewState.Error(result.message)
                        )
                    }
                }
            }
        } else {
            updateViewState(BudgetFormPlanSelectViewState.GetBudgetPlan(null))
        }
    }

    fun setSelectedCategory(category: Category) {
        selectedCategory = category

        updateViewState(
            BudgetFormPlanSelectViewState.SelectCategory(category)
        )
    }

    fun saveBudgetPlan(amount: Double) {
        viewModelScope.launch {
            when (val result = budgetRepository.saveBudgetPlan(
                BudgetPlan(
                    budgetId,
                    budgetPlanType,
                    amount,
                    selectedCategory
                )
            )) {
                is State.Success -> {
                    updateViewState(BudgetFormPlanSelectViewState.SaveBudget)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun updateBudgetPlan() {
        viewModelScope.launch {
            when (val result = budgetRepository.updateBudgetPlan(budgetPlan)) {
                is State.Success -> {
                    updateViewState(BudgetFormPlanSelectViewState.UpdateBudget)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun deleteBudgetPlan() {
        viewModelScope.launch {
            when (val result = budgetRepository.deleteBudgetPlan(budgetPlan)) {
                is State.Success -> {
                    updateViewState(BudgetFormPlanSelectViewState.DeleteBudget)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun onOpenBudgetCategoryPage() {
        updateViewState(
            BudgetFormPlanSelectViewState.OpenBudgetCategoryPage(budgetPlanType)
        )
    }

}