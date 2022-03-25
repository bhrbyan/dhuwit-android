package id.dhuwit.feature.account.ui.selection

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
class AccountSelectionViewModel @Inject constructor(
    private val accountRepository: AccountDataSource
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    init {
        getAccounts()
    }

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    private fun getAccounts() {
        viewModelScope.launch {
            when (val result = accountRepository.getAccounts()) {
                is State.Success -> {
                    updateViewState(
                        AccountSelectionViewState.GetAccounts(result.data)
                    )
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

}