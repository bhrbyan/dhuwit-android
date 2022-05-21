package id.dhuwit.feature.account.ui.main

import id.dhuwit.core.account.model.Account
import id.dhuwit.state.ViewState

sealed class AccountMainViewState : ViewState.Feature() {

    data class SetPeriodDate(val periodDate: String?) : AccountMainViewState()

    data class GetAccounts(val accounts: List<Account>?) : AccountMainViewState()

    data class UpdateAccount(val accoundId: Long?) : AccountMainViewState()

    data class GetTransactions(val incomeAmount: Double?, val expenseAmount: Double?) :
        AccountMainViewState()

}