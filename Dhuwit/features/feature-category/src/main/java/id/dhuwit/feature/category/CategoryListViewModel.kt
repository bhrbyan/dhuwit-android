package id.dhuwit.feature.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.category.repository.CategoryDataSource
import id.dhuwit.state.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val categoryRepository: CategoryDataSource
) : ViewModel() {

    private val _categories = MutableLiveData<State<List<Category>>>()
    val categories: LiveData<State<List<Category>>> = _categories

    fun getCategories(categoryType: CategoryType) {
        _categories.value = State.Loading()
        viewModelScope.launch {
            _categories.value = categoryRepository.getCategories(categoryType)
        }
    }

}