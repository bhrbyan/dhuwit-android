package id.dhuwit.feature.transaction.ui.account

import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.state.ViewState

sealed class TransactionAccountViewState : ViewState.Feature() {
    data class GetAccounts(val accounts: List<Account>?) : TransactionAccountViewState()
}
