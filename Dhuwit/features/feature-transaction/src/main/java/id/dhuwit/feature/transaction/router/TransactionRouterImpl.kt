package id.dhuwit.feature.transaction.router

import android.content.Context
import android.content.Intent
import id.dhuwit.core.transaction.model.TransactionListType
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.transaction.ui.TransactionActivity
import id.dhuwit.feature.transaction.ui.list.TransactionListActivity

internal object TransactionRouterImpl : TransactionRouter {

    const val KEY_TRANSACTION_ID: String = "transaction_id"
    const val KEY_TRANSACTION_PERIOD_DATE: String = "transaction_period_date"
    const val KEY_TRANSACTION_LIST_TYPE: String = "transaction_list_type"
    const val KEY_TRANSACTION_TYPE: String = "transaction_type"

    override fun openTransactionPage(context: Context, transactionId: Long?): Intent {
        return Intent(context, TransactionActivity::class.java).apply {
            putExtra(KEY_TRANSACTION_ID, transactionId)
        }
    }

    override fun openTransactionListPage(
        context: Context,
        periodDate: String?,
        transactionListType: TransactionListType,
        transactionType: TransactionType
    ): Intent {
        return Intent(context, TransactionListActivity::class.java).apply {
            putExtra(KEY_TRANSACTION_PERIOD_DATE, periodDate)
            putExtra(KEY_TRANSACTION_LIST_TYPE, transactionListType.toString())
            putExtra(KEY_TRANSACTION_TYPE, transactionType.toString())
        }
    }
}