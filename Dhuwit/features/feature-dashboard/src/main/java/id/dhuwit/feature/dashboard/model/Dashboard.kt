package id.dhuwit.feature.dashboard.model

import id.dhuwit.core.account.model.Account
import id.dhuwit.core.currency.model.Currency
import id.dhuwit.core.transaction.model.Transaction

data class Dashboard(
    val account: Account?,
    val transactions: List<Transaction>?
)
