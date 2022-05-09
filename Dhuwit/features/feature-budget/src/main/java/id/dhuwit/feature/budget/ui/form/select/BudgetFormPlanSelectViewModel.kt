package id.dhuwit.feature.budget.ui.form.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.budget.repository.BudgetDataSource
import id.dhuwit.core.category.model.Category
import id.dhuwit.state.ViewState
import javax.inject.Inject

@HiltViewModel
class BudgetFormPlanSelectViewModel @Inject constructor(
    private val budgetRepository: BudgetDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: Long? = savedStateHandle.get("KEY_CATEGORY_ID")

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        getData(categoryId)
    }

    private fun getData(categoryId: Long?) {
        if (categoryId != null) {
            updateViewState(BudgetFormPlanSelectViewState.SetUpViewUpdatePlan)
        } else {
            updateViewState(BudgetFormPlanSelectViewState.SetUpViewAddPlan)
        }
    }

    fun setSelectedCategory(category: Category) {
        updateViewState(
            BudgetFormPlanSelectViewState.SetSelectedCategory(category)
        )
    }

}