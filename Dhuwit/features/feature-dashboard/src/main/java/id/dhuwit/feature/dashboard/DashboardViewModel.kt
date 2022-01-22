package id.dhuwit.feature.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_PERIOD
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.repository.TransactionDataSource
import id.dhuwit.feature.dashboard.model.Dashboard
import id.dhuwit.state.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionDataSource
) : ViewModel() {

    private val _details = MutableLiveData<State<Dashboard>>()
    val details: LiveData<State<Dashboard>> = _details

    private val _periodDate = MutableLiveData<String>()
    val periodDate: LiveData<String>
        get() = _periodDate

    private var periodMonth: Int = CURRENT_MONTH

    fun getDetails() {
        _details.value = State.Loading()
        viewModelScope.launch {
            val transactions = getTransactions()

            setUpDetails(transactions)
        }
    }

    private fun updateTransactions() {
        _details.value = State.Loading(Dashboard(null))
        viewModelScope.launch {
            val transactions = getTransactions()

            setUpDetails(transactions)
        }
    }

    private suspend fun getTransactions(): State<List<Transaction>> =
        transactionRepository.getTransactions()

    private fun setUpDetails(transactions: State<List<Transaction>>) {
        if (transactions.data != null) {
            val sortedTransaction = transactions.data
                ?.filter { transaction -> isTransactionWithinPeriodDate(transaction) }
                ?.sortedByDescending { transaction -> transaction.date }

            _details.value = State.Success(
                Dashboard(transactions = sortedTransaction)
            )
        } else {
            _details.value = State.Error("${transactions.message}")
        }
    }

    private fun isTransactionWithinPeriodDate(transaction: Transaction): Boolean {
        return DateHelper.isTransactionDateWithinRangePeriodDate(
            transactionDate = transaction.date.convertPattern(
                DateHelper.PATTERN_DATE_DATABASE,
                PATTERN_DATE_PERIOD
            ),
            periodDate = _periodDate.value ?: ""
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
        _periodDate.value = DateHelper.getPeriodDate(periodMonth, PATTERN_DATE_PERIOD)
    }

    companion object {
        private const val CURRENT_MONTH: Int = 0
    }

}