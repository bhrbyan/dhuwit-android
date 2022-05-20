package id.dhuwit.feature.account.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        getAccounts()
    }

    fun getAccounts() {
        viewModelScope.launch {
            when (val result = accountRepository.getAccounts()) {
                is State.Success -> {
                    updateViewState(AccountMainViewState.GetAccounts(result.data))
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

}