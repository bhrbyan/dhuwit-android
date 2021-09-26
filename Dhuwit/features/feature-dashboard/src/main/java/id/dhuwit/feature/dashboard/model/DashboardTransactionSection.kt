package id.dhuwit.feature.dashboard.model

import com.intrusoft.sectionedrecyclerview.Section
import id.dhuwit.core.transaction.model.Transaction

class DashboardTransactionSection : Section<Transaction> {

    var date = ""
    val dateHeader: String
        get() = date

    var amountDaily = 0.0
    val amountDailyTransaction: Double
        get() = amountDaily

    var transactions = mutableListOf<Transaction>()
    private val _transaction: MutableList<Transaction>
        get() = transactions

    override fun getChildItems(): List<Transaction> {
        return _transaction
    }
}