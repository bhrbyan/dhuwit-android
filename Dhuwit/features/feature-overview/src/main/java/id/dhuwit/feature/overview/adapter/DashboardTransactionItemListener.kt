package id.dhuwit.feature.overview.adapter

import id.dhuwit.core.transaction.model.Transaction

interface DashboardTransactionItemListener {
    fun onClickTransaction(transaction: Transaction?)
}