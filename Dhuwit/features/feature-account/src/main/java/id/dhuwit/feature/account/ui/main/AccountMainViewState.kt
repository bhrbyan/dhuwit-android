package id.dhuwit.feature.account.ui.main

import id.dhuwit.core.account.model.Account
import id.dhuwit.state.ViewState

sealed class AccountMainViewState : ViewState.Feature() {

    data class GetAccounts(val accounts: List<Account>?) : AccountMainViewState()

}