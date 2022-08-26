package id.dhuwit.feature.account.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.core.base.helper.DateHelper.convertPattern
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionGetBy
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.core.transaction.repository.TransactionDataSource
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountMainViewModel @Inject constructor(
    private val accountRepository: AccountDataSource,
    private val transactionRepository: TransactionDataSource
) : ViewModel() {

    private var periodMonth: Int = CURRENT_MONTH
    private var periodDate: String? = ""

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private var accounts: List<Account>? = null
    private var account: Account? = null

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

    private fun setSelectedAccount(position: Int) {
        account = accounts?.get(position)
    }

    fun getTransactionsSelectedAccount(position: Int) {
        setSelectedAccount(position)
        getTransactions()
    }

    fun getTransactions() {
        viewModelScope.launch {
            when (val result =
                transactionRepository.getTransactions(TransactionGetBy.ByAccountId(account?.id))
            ) {
                is State.Success -> {
                    val transactions = result.data
                        ?.filter { isTransactionWithinPeriodDate(it) }
                        ?.sortedByDescending { it.date }

                    val incomeAmount = getTransactionAmount(transactions, TransactionType.Income)
                    val expenseAmount = getTransactionAmount(transactions, TransactionType.Expense)

                    updateViewState(
                        AccountMainViewState.GetTransactions(
                            incomeAmount, expenseAmount, transactions
                        )
                    )
                }
                is State.Error -> {
                    updateViewState(
                        ViewState.Error(result.message)
                    )
                }
            }
        }
    }

    fun onClickUpdateAccount() {
        updateViewState(
            AccountMainViewState.UpdateAccount(
                account?.id
            )
        )
    }

    fun setDefaultPeriodDate() {
        setPeriodDate(periodMonth)
    }

    fun onNextPeriodDate() {
        setPeriodDate(++periodMonth)
        getTransactions()
    }

    fun onPreviousPeriodDate() {
        setPeriodDate(--periodMonth)
        getTransactions()
    }

    private fun setPeriodDate(periodMonth: Int) {
        periodDate = id.dhuwit.core.base.helper.DateHelper.getPeriodDate(
            periodMonth,
            id.dhuwit.core.base.helper.DateHelper.PATTERN_DATE_PERIOD
        )
        updateViewState(
            AccountMainViewState.SetPeriodDate(periodDate)
        )
    }

    private fun getTransactionAmount(
        transactions: List<Transaction>?,
        transactionType: TransactionType
    ): Double? {
        return when (transactionType) {
            is TransactionType.Income -> transactions?.filter { it.type is TransactionType.Income }
            is TransactionType.Expense -> transactions?.filter { it.type is TransactionType.Expense }
        }?.sumOf { it.amount }
    }

    private fun isTransactionWithinPeriodDate(transaction: Transaction): Boolean {
        return id.dhuwit.core.base.helper.DateHelper.isTransactionDateWithinRangePeriodDate(
            transactionDate = transaction.date.convertPattern(
                id.dhuwit.core.base.helper.DateHelper.PATTERN_DATE_DATABASE,
                id.dhuwit.core.base.helper.DateHelper.PATTERN_DATE_PERIOD
            ),
            periodDate = periodDate ?: ""
        )
    }

    fun onCreateTransaction() {
        updateViewState(
            AccountMainViewState.CreateTransaction(account?.id)
        )
    }

    companion object {
        private const val CURRENT_MONTH: Int = 0
    }

}