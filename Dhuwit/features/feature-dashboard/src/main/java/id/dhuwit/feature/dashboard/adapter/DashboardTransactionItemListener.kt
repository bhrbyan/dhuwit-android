package id.dhuwit.feature.dashboard.adapter

import id.dhuwit.core.transaction.model.Transaction

interface DashboardTransactionItemListener {
    fun onClickTransaction(transaction: Transaction?)
}