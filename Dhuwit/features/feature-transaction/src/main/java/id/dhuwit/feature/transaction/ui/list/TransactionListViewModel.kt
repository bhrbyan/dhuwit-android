package id.dhuwit.feature.transaction.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.transaction.model.TransactionGetType
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.core.transaction.repository.TransactionDataSource
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val transactionRepository: TransactionDataSource
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun getTransactions(transactionType: TransactionType, periodDate: String?) {
        viewModelScope.launch {
            when (val result = transactionRepository.getTransactions(
                TransactionGetType.GetByTransactionType(transactionType),
                periodDate
            )) {
                is State.Success -> {
                    val totalAmountTransaction = result.data?.sumOf { it.amount } ?: 0.0
                    val totalTransaction = result.data?.size ?: 0
                    updateViewState(
                        TransactionListViewState.GetTransactions(
                            result.data,
                            totalAmountTransaction,
                            totalTransaction
                        )
                    )
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

}