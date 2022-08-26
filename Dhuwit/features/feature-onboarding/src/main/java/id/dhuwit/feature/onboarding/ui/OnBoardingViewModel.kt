package id.dhuwit.feature.onboarding.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.core.base.state.State
import id.dhuwit.core.base.state.ViewState
import id.dhuwit.core.setting.user.SettingUser
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val accountRepository: AccountDataSource,
    private val settingUser: SettingUser
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
        settingUser.setFirstTimeUser(false)
    }

}