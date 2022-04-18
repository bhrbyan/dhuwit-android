package id.dhuwit.feature.overview.model

import id.dhuwit.core.transaction.model.Transaction

data class OverviewTransaction(
    val transactions: List<Transaction>?,
    val totalIncomeTransaction: Double,
    val totalExpenseTransaction: Double
)
