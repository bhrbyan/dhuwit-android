package id.dhuwit.feature.category

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.category.repository.CategoryDataSource
import id.dhuwit.state.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val categoryRepository: CategoryDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var categoryType: CategoryType = CategoryType.getCategoryType(
        savedStateHandle.get<String>(CategoryListConstants.KEY_CATEGORY_TYPE)
    )

    private val _categories = MutableLiveData<State<List<Category>>>()
    val categories: LiveData<State<List<Category>>> = _categories

    private val _searchedCategories = MutableLiveData<List<Category>>()
    val searchedCategories: LiveData<List<Category>> = _searchedCategories

    init {
        getCategories(categoryType)
    }

    private fun getCategories(categoryType: CategoryType) {
        _categories.value = State.Loading()
        viewModelScope.launch {
            _categories.value = categoryRepository.getCategories(categoryType)
        }
    }

    fun searchCategories(keywords: String) {
        _searchedCategories.value = _categories.value?.data?.filter {
            it.name.lowercase().contains(keywords)
        }
    }

}