package id.dhuwit.feature.category.ui.budget

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategorySearch
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.category.repository.CategoryDataSource
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryBudgetListViewModel @Inject constructor(
    private val categoryRepository: CategoryDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var categories: List<Category>? = null

    private var categoryType: CategoryType = CategoryType.getCategoryType(
        savedStateHandle.get<String>(CategoryBudgetListConstants.KEY_CATEGORY_TYPE)
    )

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        getCategories(categoryType)
    }

    private fun getCategories(categoryType: CategoryType) {
        viewModelScope.launch {
            when (val result = categoryRepository.getCategories(categoryType)) {
                is State.Success -> {
                    categories = result.data
                    updateViewState(CategoryBudgetListViewState.GetCategories(result.data))
                }
                is State.Error -> {
                    updateViewState(
                        ViewState.Error(result.message)
                    )
                }
            }
        }
    }

    fun searchCategories(keywords: String) {
        updateViewState(
            CategoryBudgetListViewState.SearchCategory(
                CategorySearch(
                    keywords = keywords,
                    categories = categories?.filter { it.name.lowercase().contains(keywords) }
                )
            )
        )
    }

}