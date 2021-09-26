package id.dhuwit.feature.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.state.State
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

    private var _isFieldEmpty = MutableLiveData<Boolean>()
    val isFieldEmpty: LiveData<Boolean> = _isFieldEmpty

    private var _createAccount = MutableLiveData<State<Boolean>>()
    val createAccount: LiveData<State<Boolean>> = _createAccount

    fun setAccountName(name: String) {
        this.name = name
    }

    fun setAccountBalance(balance: Double) {
        this.balance = balance
    }

    fun checkInputField() {
        _isFieldEmpty.value = name.isEmpty() && balance == 0.0
    }

    fun createAccount() {
        _createAccount.value = State.Loading()
        viewModelScope.launch {
            _createAccount.value = accountRepository.storeAccount(Account(name, balance))
        }
    }

    fun updateStatusFirstTimeUser() {
        storage.setFirstTimeUser(false)
    }

}