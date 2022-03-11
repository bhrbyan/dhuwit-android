package id.dhuwit.feature.account.ui.form

import id.dhuwit.core.account.model.Account
import id.dhuwit.state.ViewState

sealed class AccountViewState : ViewState.Feature() {
    object CreateAccount : AccountViewState()
    object Success : AccountViewState()
    data class GetAccount(val account: Account?, val accountsMoreThanOne: Boolean) :
        AccountViewState()

}
