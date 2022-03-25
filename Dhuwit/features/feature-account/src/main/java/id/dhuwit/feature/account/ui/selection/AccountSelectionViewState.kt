package id.dhuwit.feature.account.ui.selection

import id.dhuwit.core.account.model.Account
import id.dhuwit.state.ViewState

sealed class AccountSelectionViewState : ViewState.Feature() {
    data class GetAccounts(val accounts: List<Account>?) : AccountSelectionViewState()
}
