package id.dhuwit.feature.transaction.router

import android.content.Context
import android.content.Intent
import id.dhuwit.core.transaction.model.TransactionListType
import id.dhuwit.core.transaction.model.TransactionType

interface TransactionRouter {
    fun openTransactionPage(context: Context, transactionId: Long?): Intent
    fun openTransactionListPage(
        context: Context,
        periodDate: String?,
        transactionListType: TransactionListType,
        transactionType: TransactionType? = null,
        categoryId: Long? = null,
        accountId: Long? = null
    ): Intent
}