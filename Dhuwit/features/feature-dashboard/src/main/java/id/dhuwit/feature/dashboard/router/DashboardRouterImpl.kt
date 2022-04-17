package id.dhuwit.feature.dashboard.router

import androidx.fragment.app.Fragment
import id.dhuwit.feature.dashboard.ui.dashboard.DashboardFragment

object DashboardRouterImpl : DashboardRouter {
    override fun openDashboardPage(): Fragment {
        return DashboardFragment()
    }
}