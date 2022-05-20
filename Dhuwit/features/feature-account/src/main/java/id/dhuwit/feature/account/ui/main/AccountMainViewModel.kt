package id.dhuwit.feature.account.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountMainViewModel @Inject constructor(private val accountRepository: AccountDataSource) :
    ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private var accounts: List<Account>? = null
    private var selectedAccountPosition: Int = -1

    init {
        getAccounts()
    }

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    private fun setAccounts(accounts: List<Account>?) {
        this.accounts = accounts
    }

    fun getAccounts() {
        viewModelScope.launch {
            when (val result = accountRepository.getAccounts()) {
                is State.Success -> {
                    setAccounts(result.data)
                    updateViewState(
                        AccountMainViewState.GetAccounts(accounts)
                    )
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    private fun setSelectedAccountPosition(position: Int) {
        selectedAccountPosition = position
    }

    fun getDetailSelectedAccount(position: Int) {
        setSelectedAccountPosition(position)
        // Do something
    }

    fun onClickUpdateAccount() {
        updateViewState(
            AccountMainViewState.UpdateAccount(
                accounts?.get(selectedAccountPosition)?.id
            )
        )
    }

}