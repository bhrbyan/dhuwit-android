package id.dhuwit.feature.account.ui.form

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.feature.account.router.AccountRouterImpl.KEY_ACCOUNT_ID
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var accountId: Long = savedStateHandle.get<Long>(KEY_ACCOUNT_ID) ?: DEFAULT_ACCOUNT_ID
    private var account: Account? = null

    private var _viewState = MutableLiveData<ViewState>()
    var viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        if (accountId != DEFAULT_ACCOUNT_ID) {
            getAccount(accountId)
        } else {
            updateViewState(AccountViewState.CreateAccount)
        }
    }

    private fun getAccount(id: Long) {
        viewModelScope.launch {
            when (val result = accountRepository.getAccounts()) {
                is State.Success -> {
                    result.data?.let { accounts ->
                        account = accounts.find { it.id == id }
                        val accountsMoreThanOne = accounts.size > 1
                        updateViewState(AccountViewState.GetAccount(account, accountsMoreThanOne))
                    }
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun createAccount(name: String, balance: Double, isPrimary: Boolean) {
        viewModelScope.launch {
            when (val result = accountRepository.createAccount(Account(name, balance, isPrimary))) {
                is State.Success -> {
                    updateViewState(AccountViewState.Success)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun updateAccount(name: String, balance: Double, isPrimary: Boolean) {
        viewModelScope.launch {
            when (val result =
                accountRepository.updateAccount(Account(name, balance, isPrimary, account?.id))) {
                is State.Success -> {
                    updateViewState(AccountViewState.Success)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            when (val result = accountRepository.deleteAccount(account?.id)) {
                is State.Success -> {
                    updateViewState(AccountViewState.Success)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_ACCOUNT_ID: Long = -1
    }

}