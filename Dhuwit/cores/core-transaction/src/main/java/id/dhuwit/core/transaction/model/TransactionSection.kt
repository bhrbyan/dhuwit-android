package id.dhuwit.core.transaction.model

class TransactionSection {
    var date: String = ""
    var totalAmount: Double = 0.0
    var transactions: MutableList<Transaction> = mutableListOf()
}
