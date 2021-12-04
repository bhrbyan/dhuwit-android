package id.dhuwit.feature.account.router

import android.content.Context
import android.content.Intent
import id.dhuwit.feature.account.ui.form.AccountActivity

internal object AccountRouterImpl : AccountRouter {

    const val KEY_ACCOUNT_ID: String = "account_id"

    override fun openAccountPage(context: Context, accountId: Long?): Intent {
        return Intent(context, AccountActivity::class.java).apply {
            putExtra(KEY_ACCOUNT_ID, accountId)
        }
    }
}