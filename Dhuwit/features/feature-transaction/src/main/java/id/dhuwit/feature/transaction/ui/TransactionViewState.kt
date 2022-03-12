package id.dhuwit.feature.transaction.ui

import id.dhuwit.state.ViewState

sealed class TransactionViewState : ViewState.Feature() {

    object SetUpViewNewTransaction : TransactionViewState()

    object SetUpViewUpdateTransaction : TransactionViewState()

    data class SetAmount(val amount: Double?) : TransactionViewState()

}