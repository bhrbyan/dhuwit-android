package id.dhuwit.feature.overview.adapter

import id.dhuwit.core.transaction.model.Transaction

interface OverviewTransactionItemListener {
    fun onClickTransaction(transaction: Transaction?)
}