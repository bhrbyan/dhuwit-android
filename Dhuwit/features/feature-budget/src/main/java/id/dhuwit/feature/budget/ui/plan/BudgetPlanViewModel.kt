package id.dhuwit.feature.budget.ui.plan

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.category.repository.CategoryDataSource
import id.dhuwit.feature.budget.ui.BudgetConstants.KEY_CATEGORY_TYPE
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetPlanViewModel @Inject constructor(
    private val categoryRepository: CategoryDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

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
            when (val result = categoryRepository.getCategories(categoryType)) {
                is State.Success -> {
                    updateViewState(
                        BudgetPlanViewState.GetCategories(result.data)
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

}