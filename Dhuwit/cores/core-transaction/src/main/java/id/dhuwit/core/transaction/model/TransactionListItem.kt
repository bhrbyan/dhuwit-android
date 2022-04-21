package id.dhuwit.core.transaction.model

class TransactionListItem {
    var date: String = ""
    var totalAmount: Double = 0.0
    var transactions: MutableList<Transaction> = mutableListOf()
}
