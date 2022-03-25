package id.dhuwit.feature.transaction.router

import android.content.Context
import android.content.Intent
import id.dhuwit.feature.transaction.ui.TransactionActivity

internal object TransactionRouterImpl : TransactionRouter {

    const val KEY_TRANSACTION_ID: String = "transaction_id"

    override fun openTransactionPage(context: Context, transactionId: Long?): Intent {
        return Intent(context, TransactionActivity::class.java).apply {
            putExtra(KEY_TRANSACTION_ID, transactionId)
        }
    }
}