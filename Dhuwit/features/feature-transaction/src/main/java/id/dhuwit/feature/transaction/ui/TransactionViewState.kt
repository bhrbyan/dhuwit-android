package id.dhuwit.feature.transaction.ui

import id.dhuwit.state.ViewState

sealed class TransactionViewState : ViewState.Feature() {

    object SuccessSaveTransaction : TransactionViewState()

    object ErrorAccountEmpty : TransactionViewState()

}