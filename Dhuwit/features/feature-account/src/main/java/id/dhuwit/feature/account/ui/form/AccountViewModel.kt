package id.dhuwit.feature.account.ui.form

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.feature.account.router.AccountRouterImpl.KEY_ACCOUNT_ID
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
    private var isPrimary: Boolean = false

    private var _accountId: Long = savedStateHandle.get<Long>(KEY_ACCOUNT_ID) ?: DEFAULT_ACCOUNT_ID

    private var _isFieldEmpty = MutableLiveData<Boolean>()
    val isFieldEmpty: LiveData<Boolean> = _isFieldEmpty

    private val _account = MutableLiveData<State<Account>>()
    val account: LiveData<State<Account>> = _account

    private var _action = MutableLiveData<State<Boolean>>()
    val action: LiveData<State<Boolean>> = _action

    init {
        getAccount(_accountId)
    }

    private fun getAccount(id: Long) {
        viewModelScope.launch {
            _account.value = accountRepository.getAccount(id)
        }
    }

    fun setAccountName(name: String) {
        this.name = name
    }

    fun setAccountBalance(balance: Double) {
        this.balance = balance
    }

    fun setStatusPrimaryAccount(isPrimary: Boolean) {
        this.isPrimary = isPrimary
    }

    fun checkInputField() {
        _isFieldEmpty.value = name.isEmpty() && balance == 0.0
    }

    fun createAccount() {
        _action.value = State.Loading()
        viewModelScope.launch {
            _action.value = accountRepository.storeAccount(Account(name, balance, isPrimary))
        }
    }

    fun updateAccount() {
        _action.value = State.Loading()
        viewModelScope.launch {
            _action.value = accountRepository.updateAccount(
                Account(name, balance, isPrimary, _account.value?.data?.id ?: 0)
            )
        }
    }

    fun deleteAccount() {
        _action.value = State.Loading()
        viewModelScope.launch {
            _action.value = accountRepository.deleteAccount(_account.value?.data?.id ?: 0)
        }
    }

    companion object {
        private const val DEFAULT_ACCOUNT_ID: Long = -1
    }

}