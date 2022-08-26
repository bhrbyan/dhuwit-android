package id.dhuwit.feature.account.ui.form

import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.state.ViewState

sealed class AccountFormViewState : ViewState.Feature() {
    object CreateAccount : AccountFormViewState()
    object Success : AccountFormViewState()
    data class GetAccount(val account: Account?, val accountsMoreThanOne: Boolean) :
        AccountFormViewState()

}
