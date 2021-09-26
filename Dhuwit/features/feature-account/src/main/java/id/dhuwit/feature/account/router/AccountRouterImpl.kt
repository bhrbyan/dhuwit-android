package id.dhuwit.feature.account.router

import android.content.Context
import android.content.Intent
import id.dhuwit.feature.account.AccountActivity

internal object AccountRouterImpl : AccountRouter {
    override fun openAccountPage(context: Context): Intent {
        return Intent(context, AccountActivity::class.java)
    }
}