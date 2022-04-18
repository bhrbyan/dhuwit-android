package id.dhuwit.feature.overview.ui.transaction.adapter

import id.dhuwit.core.transaction.model.Transaction

interface OverviewTransactionItemListener {
    fun onClickTransaction(transaction: Transaction?)
}