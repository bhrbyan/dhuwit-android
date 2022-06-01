package id.dhuwit.feature.account.router

import android.content.Context
import android.content.Intent
import id.dhuwit.feature.account.ui.form.AccountFormActivity
import id.dhuwit.feature.account.ui.main.AccountMainActivity

internal object AccountRouterImpl : AccountRouter {

    const val KEY_ACCOUNT_ID: String = "account_id"

    override fun openAccountMainPage(context: Context): Intent {
        return Intent(context, AccountMainActivity::class.java)
    }

    override fun openAccountFormPage(context: Context, accountId: Long?): Intent {
        return Intent(context, AccountFormActivity::class.java).apply {
            putExtra(KEY_ACCOUNT_ID, accountId)
        }
    }
}