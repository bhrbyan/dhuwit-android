package id.dhuwit.feature.transaction.router

import android.content.Context
import android.content.Intent

interface TransactionRouter {
    fun openTransactionPage(context: Context, transactionId: Long?): Intent
}