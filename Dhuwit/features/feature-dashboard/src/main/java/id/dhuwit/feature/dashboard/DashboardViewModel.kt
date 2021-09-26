package id.dhuwit.feature.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.core.transaction.repository.TransactionDataSource
import id.dhuwit.feature.dashboard.model.Dashboard
import id.dhuwit.state.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val accountRepository: AccountDataSource,
    private val transactionRepository: TransactionDataSource
) : ViewModel() {

    private val _details = MutableLiveData<State<Dashboard>>()
    val details: LiveData<State<Dashboard>> = _details

    fun getDetails() {
        _details.value = State.Loading()
        viewModelScope.launch {
            val account = accountRepository.getAccount()
            val transactions = transactionRepository.getTransactions()

            if (account.data != null && transactions.data != null) {
                val sortedTransaction = transactions.data?.sortedByDescending { transaction ->
                    transaction.date
                }
                _details.value = State.Success(
                    Dashboard(
                        account = account.data,
                        transactions = sortedTransaction
                    )
                )
            } else {
                _details.value = State.Error(
                    "${account.message} ${transactions.message}"
                )
            }
        }
    }
}