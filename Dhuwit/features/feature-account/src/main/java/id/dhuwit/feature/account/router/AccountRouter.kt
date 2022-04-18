package id.dhuwit.feature.account.router

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

interface AccountRouter {
    fun openAccountPage(context: Context, accountId: Long?): Intent
    fun openAccountSelectionPage(context: Context): Intent
    fun openAccountListPage(): Fragment
}