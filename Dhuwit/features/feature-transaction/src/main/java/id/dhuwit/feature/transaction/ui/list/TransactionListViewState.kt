package id.dhuwit.feature.transaction.ui.list

import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.state.ViewState

sealed class TransactionListViewState : ViewState.Feature() {

    data class GetTransactions(
        val transactions: List<Transaction>?,
        val totalAmountTransaction: Double,
        val totalTransaction: Int
    ) : TransactionListViewState()

}
