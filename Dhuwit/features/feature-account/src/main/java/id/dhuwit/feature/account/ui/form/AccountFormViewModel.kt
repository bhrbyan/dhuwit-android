package id.dhuwit.feature.account.ui.form

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.core.extension.convertDoubleToString
import id.dhuwit.feature.account.router.AccountRouterImpl.KEY_ACCOUNT_ID
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountFormViewModel @Inject constructor(
    private val accountRepository: AccountDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var accountId: Long = savedStateHandle.get<Long>(KEY_ACCOUNT_ID) ?: DEFAULT_ACCOUNT_ID
    private var account: Account? = null

    private var counterAmount: String = DEFAULT_AMOUNT.convertDoubleToString()

    private var _viewState = MutableLiveData<ViewState>()
    var viewState: LiveData<ViewState> = _viewState

    private var _amount = MutableLiveData<Double?>()
    var amount: LiveData<Double?> = _amount

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        if (accountId != DEFAULT_ACCOUNT_ID) {
            getAccount(accountId)
        } else {
            setCounter(DEFAULT_AMOUNT.convertDoubleToString(), false)
            updateViewState(AccountFormViewState.CreateAccount)
        }
    }

    private fun getAccount(id: Long) {
        viewModelScope.launch {
            when (val result = accountRepository.getAccounts()) {
                is State.Success -> {
                    result.data?.let { accounts ->
                        account = accounts.find { it.id == id }
                        val accountsMoreThanOne = accounts.size > 1
                        setCounter(account?.balance?.convertDoubleToString(), false)
                        updateViewState(
                            AccountFormViewState.GetAccount(
                                account,
                                accountsMoreThanOne
                            )
                        )
                    }
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    private fun setAmount(amount: Double?) {
        _amount.value = amount
    }

    fun setCounter(counter: String?, isInputFromKeyboard: Boolean) {
        var fullCounter: String = counterAmount
        fullCounter += counter

        if (isCounterLengthMoreThanOne(fullCounter) && isFirstCharZero(fullCounter)) {
            fullCounter = removeFirstChar(fullCounter)
        }

        if (isInputFromKeyboard) {
            if (isCounterLengthLessThanEqualHundredBillion(fullCounter)) {
                counterAmount = fullCounter
                setAmount(counterAmount.toDouble())
            }
        } else {
            counterAmount = fullCounter
            setAmount(counterAmount.toDouble())
        }
    }

    private fun isCounterLengthLessThanEqualHundredBillion(fullCounter: String): Boolean {
        return fullCounter.length <= 9
    }

    private fun isFirstCharZero(counter: String): Boolean {
        return counter.first() == '0'
    }

    private fun isCounterLengthMoreThanOne(counter: String): Boolean {
        return counter.length > 1
    }

    private fun removeFirstChar(counter: String): String {
        return counter.removePrefix("0")
    }

    fun onClearCounter() {
        val counter = counterAmount.dropLast(1)

        counterAmount = counter
        val finalAmount = if (counter.isEmpty()) {
            DEFAULT_AMOUNT
        } else {
            counter.toDouble()
        }

        setAmount(finalAmount)
    }

    fun createAccount(name: String, isPrimary: Boolean) {
        viewModelScope.launch {
            when (val result = accountRepository.createAccount(
                Account(
                    name,
                    amount.value ?: DEFAULT_AMOUNT,
                    isPrimary
                )
            )) {
                is State.Success -> {
                    updateViewState(AccountFormViewState.Success)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun updateAccount(name: String, isPrimary: Boolean) {
        viewModelScope.launch {
            when (val result =
                accountRepository.updateAccount(
                    Account(
                        name,
                        amount.value ?: DEFAULT_AMOUNT,
                        isPrimary,
                        account?.id
                    )
                )) {
                is State.Success -> {
                    updateViewState(AccountFormViewState.Success)
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
                    updateViewState(AccountFormViewState.Success)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_ACCOUNT_ID: Long = -1
        private const val DEFAULT_AMOUNT: Double = 0.0
    }

}