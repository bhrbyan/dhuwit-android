package id.dhuwit.feature.onboarding.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val accountRepository: AccountDataSource,
    private val storage: Storage
) : ViewModel() {

    private var _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun createAccount(account: Account) {
        viewModelScope.launch {
            when (val result = accountRepository.createAccount(account)) {
                is State.Success -> {
                    updateStatusFirstTimeUser()
                    updateViewState(OnBoardingViewState.SuccessCreateAccount)
                }
                is State.Error -> {
                    updateViewState(
                        ViewState.Error(result.message)
                    )
                }
            }
        }
    }

    private fun updateStatusFirstTimeUser() {
        storage.setFirstTimeUser(false)
    }

}