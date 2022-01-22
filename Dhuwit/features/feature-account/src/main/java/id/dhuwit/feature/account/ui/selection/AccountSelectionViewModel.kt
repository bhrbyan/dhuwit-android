package id.dhuwit.feature.account.ui.selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.state.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSelectionViewModel @Inject constructor(
    private val accountRepository: AccountDataSource
) : ViewModel() {

    private val _accounts = MutableLiveData<State<List<Account>>>()
    val accounts: LiveData<State<List<Account>>> = _accounts

    init {
        getAccounts()
    }

    private fun getAccounts() {
        viewModelScope.launch {
            _accounts.value = accountRepository.getAccounts()
        }
    }

}