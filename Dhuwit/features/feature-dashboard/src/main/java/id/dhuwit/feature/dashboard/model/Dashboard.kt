package id.dhuwit.feature.dashboard.model

import id.dhuwit.core.transaction.model.Transaction

data class Dashboard(
    val transactions: List<Transaction>?,
    val overviewIncome: Double,
    val overviewExpense: Double
)
