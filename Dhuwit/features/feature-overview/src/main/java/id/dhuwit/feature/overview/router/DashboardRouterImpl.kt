package id.dhuwit.feature.overview.router

import androidx.fragment.app.Fragment
import id.dhuwit.feature.overview.ui.dashboard.DashboardFragment

object DashboardRouterImpl : DashboardRouter {
    override fun openDashboardPage(): Fragment {
        return DashboardFragment()
    }
}