package id.dhuwit.feature.category

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategorySearch
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
    private val _searchedCategories = MutableLiveData<CategorySearch>()
    private val _addCategory = MutableLiveData<State<Category>>()

    val categories: LiveData<State<List<Category>>> = _categories
    val searchedCategories: LiveData<CategorySearch> = _searchedCategories
    val addCategory: LiveData<State<Category>> = _addCategory

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
        _searchedCategories.value = CategorySearch(
            keywords = keywords,
            categories = _categories.value?.data?.filter { it.name.lowercase().contains(keywords) }
        )
    }

    fun addCategory(categoryName: String) {
        _addCategory.value = State.Loading()
        viewModelScope.launch {
            _addCategory.value = categoryRepository.addCategory(
                Category(categoryName, categoryType)
            )
        }
    }

}