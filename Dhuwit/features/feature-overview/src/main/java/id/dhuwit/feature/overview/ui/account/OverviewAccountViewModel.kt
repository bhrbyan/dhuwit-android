package id.dhuwit.feature.overview.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.transaction.model.TransactionAccount
import id.dhuwit.core.transaction.repository.TransactionDataSource
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewAccountViewModel @Inject constructor(
    private val transactionRepository: TransactionDataSource
) : ViewModel() {

    private var periodMonth: Int = CURRENT_MONTH
    private var periodDate: String? = ""

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    init {
        setPeriodDate(periodMonth)
        getTransactions(periodDate)
    }

    private fun getTransactions(periodDate: String?) {
        viewModelScope.launch {
            when (val result = transactionRepository.getAccountTransaction(periodDate)) {
                is State.Success -> {
                    setUpListAccounts(result.data)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    private fun setUpListAccounts(transactions: List<TransactionAccount>?) {
        if (transactions != null) {
            updateViewState(
                OverviewAccountViewState.GetDetails(transactions, periodDate)
            )
        } else {
            updateViewState(OverviewAccountViewState.CategoryNotFound)
        }
    }

    fun onNextPeriodDate() {
        setPeriodDate(++periodMonth)
        getTransactions(periodDate)
    }

    fun onPreviousPeriodDate() {
        setPeriodDate(--periodMonth)
        getTransactions(periodDate)
    }

    private fun setPeriodDate(periodDate: Int) {
        this.periodDate = DateHelper.getPeriodDate(periodDate, DateHelper.PATTERN_DATE_PERIOD)
    }

    fun openTransactionListPage(accountId: Long) {
        updateViewState(
            OverviewAccountViewState.OpenTransactionListPage(periodDate, accountId)
        )
    }

    companion object {
        private const val CURRENT_MONTH: Int = 0
    }

}