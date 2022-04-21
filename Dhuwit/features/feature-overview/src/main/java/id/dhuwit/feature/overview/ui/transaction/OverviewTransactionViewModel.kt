package id.dhuwit.feature.overview.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_PERIOD
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.core.transaction.repository.TransactionDataSource
import id.dhuwit.feature.overview.model.OverviewTransaction
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionDataSource
) : ViewModel() {

    private var periodMonth: Int = CURRENT_MONTH
    private var periodDate: String? = ""

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun getOverview() {
        viewModelScope.launch {
            when (val result = getTransactions()) {
                is State.Success -> {
                    setUpDetails(result.data)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    private fun updateTransactions() {
        viewModelScope.launch {
            when (val result = getTransactions()) {
                is State.Success -> {
                    setUpDetails(result.data)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    private suspend fun getTransactions(): State<List<Transaction>> =
        transactionRepository.getTransactions()

    private fun setUpDetails(transactions: List<Transaction>?) {
        if (transactions != null) {
            val sortedTransaction = transactions
                .filter { transaction -> isTransactionWithinPeriodDate(transaction) }
                .sortedByDescending { transaction -> transaction.date }

            val overviewIncome = sortedTransaction
                .filter { transaction -> transaction.type is TransactionType.Income }
                .sumOf { transaction -> transaction.amount }

            val overviewExpense = sortedTransaction
                .filter { transaction -> transaction.type is TransactionType.Expense }
                .sumOf { transaction -> transaction.amount }

            updateViewState(
                OverviewTransactionViewState.GetOverview(
                    OverviewTransaction(
                        transactions = sortedTransaction,
                        totalIncomeTransaction = overviewIncome,
                        totalExpenseTransaction = overviewExpense
                    )
                )
            )
        } else {
            updateViewState(OverviewTransactionViewState.TransactionNotFound)
        }
    }

    private fun isTransactionWithinPeriodDate(transaction: Transaction): Boolean {
        return DateHelper.isTransactionDateWithinRangePeriodDate(
            transactionDate = transaction.date.convertPattern(
                DateHelper.PATTERN_DATE_DATABASE,
                PATTERN_DATE_PERIOD
            ),
            periodDate = periodDate ?: ""
        )
    }

    fun setDefaultPeriodDate() {
        setPeriodDate(periodMonth)
    }

    fun onNextPeriodDate() {
        setPeriodDate(++periodMonth)
        updateTransactions()
    }

    fun onPreviousPeriodDate() {
        setPeriodDate(--periodMonth)
        updateTransactions()
    }

    private fun setPeriodDate(periodMonth: Int) {
        periodDate = DateHelper.getPeriodDate(periodMonth, PATTERN_DATE_PERIOD)
        updateViewState(
            OverviewTransactionViewState.SetPeriodDate(periodDate)
        )
    }

    fun openTransactionListPage(transactionType: TransactionType) {
        updateViewState(
            OverviewTransactionViewState.OpenTransactionListPage(
                periodDate,
                transactionType
            )
        )
    }

    companion object {
        private const val CURRENT_MONTH: Int = 0
    }

}