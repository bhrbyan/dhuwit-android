package id.dhuwit.feature.dashboard.router

import android.content.Context
import android.content.Intent
import id.dhuwit.feature.dashboard.ui.DashboardActivity

internal object DashboardRouterImpl : DashboardRouter {
    override fun openDashboardPage(context: Context): Intent {
        return Intent(context, DashboardActivity::class.java)
    }
}