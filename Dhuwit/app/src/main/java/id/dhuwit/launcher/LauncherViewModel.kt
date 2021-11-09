package id.dhuwit.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.repository.CategoryDataSource
import id.dhuwit.core.currency.model.Currency
import id.dhuwit.core.currency.repository.CurrencyDataSource
import id.dhuwit.state.State
import id.dhuwit.storage.Storage
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val storage: Storage,
    private val currencyRepository: CurrencyDataSource,
    private val categoryRepository: CategoryDataSource
) : ViewModel() {

    private val _storeCurrency = MutableLiveData<State<Boolean>>()
    private val _storeCategory = MutableLiveData<State<Boolean>>()

    private val _storeData = MutableLiveData<Boolean>()
    val storeData: LiveData<Boolean> = _storeData

    fun storeDefaultData(currencies: List<Currency>, categories: List<Category>) {
        viewModelScope.launch {
            _storeCurrency.value = currencyRepository.storeCurrencies(currencies)
            _storeCategory.value = categoryRepository.storeCategories(categories)

            setCurrencySymbol(currencies.first())

            _storeData.value = isStoreDataSuccess()
        }
    }

    private fun setCurrencySymbol(currency: Currency) {
        storage.setSymbolCurrency(currency.symbol)
    }

    private fun isStoreDataSuccess(): Boolean {
        return _storeCurrency.value?.data == true && _storeCategory.value?.data == true
    }

}