package id.dhuwit.feature.onboarding

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

    private var name: String = ""
    private var balance: Double = 0.0

    private var _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun setAccountName(name: String) {
        this.name = name
    }

    fun setAccountBalance(balance: Double) {
        this.balance = balance
    }

    fun checkInputField() {
        updateViewState(
            OnBoardingViewState.ValidationRequirement(
                name.isEmpty() && balance == 0.0
            )
        )
    }

    fun createAccount() {
        viewModelScope.launch {
            when (val result = accountRepository.createAccount(Account(name, balance, true))) {
                is State.Success -> {
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

    fun updateStatusFirstTimeUser() {
        storage.setFirstTimeUser(false)
    }

}