package id.dhuwit.feature.budget.ui.plan

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.model.BudgetPlanItem
import id.dhuwit.core.budget.repository.BudgetDataSource
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.category.repository.CategoryDataSource
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_CATEGORY_TYPE
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetPlanViewModel @Inject constructor(
    private val budgetRepository: BudgetDataSource,
    private val categoryRepository: CategoryDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val budgetPlanItems: MutableList<BudgetPlanItem> = mutableListOf()
    private val categoryType: String? = savedStateHandle.get(KEY_CATEGORY_TYPE)

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        getCategories(
            CategoryType.getCategoryType(categoryType)
        )
    }

    private fun getCategories(categoryType: CategoryType) {
        viewModelScope.launch {
            val budgetPlans = if (categoryType is CategoryType.Income) {
                budgetRepository.budgetPlanIncomesTemp
            } else {
                budgetRepository.budgetPlanExpensesTemp
            }

            when (val result = categoryRepository.getCategories(categoryType)) {
                is State.Success -> {
                    // Move list to hashmap to prevent duplicate looping
                    val mapOfBudgetPlan: HashMap<Long, Double> = hashMapOf()
                    budgetPlans.forEach {
                        mapOfBudgetPlan[it.category.id] = it.amount
                    }

                    val categories = result.data
                    categories?.forEach { category ->
                        budgetPlanItems.add(
                            BudgetPlanItem(
                                categoryId = category.id,
                                categoryName = category.name,
                                amount = mapOfBudgetPlan[category.id] ?: 0.0
                            )
                        )
                    }

                    updateViewState(
                        BudgetPlanViewState.GetBudgetPlans(budgetPlanItems)
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

    fun updateAmount(categoryId: Long?, amount: Double?) {
        budgetPlanItems.find { it.categoryId == categoryId }?.amount = amount
        updateViewState(
            BudgetPlanViewState.UpdateAmount(categoryId, budgetPlanItems)
        )
    }

}