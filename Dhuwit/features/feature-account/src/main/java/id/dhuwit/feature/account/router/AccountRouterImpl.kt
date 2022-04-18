package id.dhuwit.feature.account.router

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import id.dhuwit.feature.account.ui.form.AccountActivity
import id.dhuwit.feature.account.ui.list.AccountListFragment
import id.dhuwit.feature.account.ui.selection.AccountSelectionActivity

internal object AccountRouterImpl : AccountRouter {

    const val KEY_ACCOUNT_ID: String = "account_id"

    override fun openAccountPage(context: Context, accountId: Long?): Intent {
        return Intent(context, AccountActivity::class.java).apply {
            putExtra(KEY_ACCOUNT_ID, accountId)
        }
    }

    override fun openAccountSelectionPage(context: Context): Intent {
        return Intent(context, AccountSelectionActivity::class.java)
    }

    override fun openAccountListPage(): Fragment {
        return AccountListFragment()
    }
}