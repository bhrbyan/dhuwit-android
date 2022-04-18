package id.dhuwit.feature.overview.model

import id.dhuwit.core.transaction.model.Transaction

data class Dashboard(
    val transactions: List<Transaction>?,
    val overviewIncome: Double,
    val overviewExpense: Double
)
