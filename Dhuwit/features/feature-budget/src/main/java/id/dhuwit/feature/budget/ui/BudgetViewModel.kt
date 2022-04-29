package id.dhuwit.feature.budget.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.repository.BudgetDataSource
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetDataSource
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        getBudgets()
    }

    private fun getBudgets() {
        viewModelScope.launch {
            when (val result = budgetRepository.getBudgets()) {
                is State.Success -> {
                    val state = if (result.data?.isNullOrEmpty() == true) {
                        BudgetViewState.ShowEmptyState
                    } else {
                        BudgetViewState.GetBudget(result.data?.first())
                    }

                    updateViewState(state)
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