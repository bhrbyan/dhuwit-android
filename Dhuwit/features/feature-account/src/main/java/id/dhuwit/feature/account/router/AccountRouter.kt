package id.dhuwit.feature.account.router

import android.content.Context
import android.content.Intent

interface AccountRouter {
    fun openAccountPage(context: Context): Intent
}