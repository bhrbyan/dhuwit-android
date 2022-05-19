package id.dhuwit.feature.account.router

import android.content.Context
import android.content.Intent

interface AccountRouter {
    fun openAccountFormPage(context: Context, accountId: Long?): Intent
    fun openAccountSelectionPage(context: Context): Intent
}