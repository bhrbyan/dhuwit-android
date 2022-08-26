package id.dhuwit.feature.account.ui.main

import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.state.ViewState
import id.dhuwit.core.transaction.model.Transaction

sealed class AccountMainViewState : ViewState.Feature() {

    data class SetPeriodDate(val periodDate: String?) : AccountMainViewState()

    data class GetAccounts(val accounts: List<Account>?) : AccountMainViewState()

    data class UpdateAccount(val accountId: Long?) : AccountMainViewState()

    data class GetTransactions(
        val incomeAmount: Double?,
        val expenseAmount: Double?,
        val transactions: List<Transaction>?
    ) :
        AccountMainViewState()

    data class CreateTransaction(val accountId: Long?) : AccountMainViewState()

}