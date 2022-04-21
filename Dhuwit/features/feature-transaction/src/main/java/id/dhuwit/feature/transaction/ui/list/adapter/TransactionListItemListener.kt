package id.dhuwit.feature.transaction.ui.list.adapter

import id.dhuwit.core.transaction.model.Transaction

interface TransactionListItemListener {
    fun onClickTransaction(transaction: Transaction?)
}