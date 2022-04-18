package id.dhuwit.feature.overview.router

import androidx.fragment.app.Fragment
import id.dhuwit.feature.overview.ui.overview.OverviewFragment

object DashboardRouterImpl : DashboardRouter {
    override fun openDashboardPage(): Fragment {
        return OverviewFragment()
    }
}