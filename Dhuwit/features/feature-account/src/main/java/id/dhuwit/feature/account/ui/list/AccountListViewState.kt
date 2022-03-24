package id.dhuwit.feature.account.ui.list

import id.dhuwit.core.account.model.Account
import id.dhuwit.state.ViewState

sealed class AccountListViewState : ViewState.Feature() {
    data class GetAccounts(val accounts: List<Account>?) : AccountListViewState()
}
