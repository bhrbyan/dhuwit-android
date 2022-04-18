package id.dhuwit.feature.overview.model

import id.dhuwit.core.transaction.model.Transaction

class OverviewTransactionItem {
    var date: String = ""
    var totalAmount: Double = 0.0
    var transactions: MutableList<Transaction> = mutableListOf()
}
