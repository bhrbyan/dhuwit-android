package id.dhuwit.feature.account.ui.main.adapter.transaction.item

import id.dhuwit.core.transaction.model.Transaction

interface AccountMainTransactionItemListener {
    fun onClickTransaction(transaction: Transaction?)
}