package id.dhuwit.feature.account

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.state.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var name: String = ""
    private var balance: Double = 0.0

    private var _isFieldEmpty = MutableLiveData<Boolean>()
    val isFieldEmpty: LiveData<Boolean> = _isFieldEmpty

    private val _account = MutableLiveData<State<Account>>()
    val account: LiveData<State<Account>> = _account

    private var _updateAccount = MutableLiveData<State<Boolean>>()
    val updateAccount: LiveData<State<Boolean>> = _updateAccount

    init {
        getAccount()
    }

    private fun getAccount() {
        viewModelScope.launch {
            _account.value = accountRepository.getAccount()
        }
    }

    fun setAccountName(name: String) {
        this.name = name
    }

    fun setAccountBalance(balance: Double) {
        this.balance = balance
    }

    fun checkInputField() {
        _isFieldEmpty.value = name.isEmpty() && balance == 0.0
    }

    fun saveAccount() {
        _updateAccount.value = State.Loading()
        viewModelScope.launch {
            _updateAccount.value = accountRepository.updateAccount(
                Account(name, balance, _account.value?.data?.id ?: 0)
            )
        }
    }

}