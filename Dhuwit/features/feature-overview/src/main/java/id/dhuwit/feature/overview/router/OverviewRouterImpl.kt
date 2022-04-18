package id.dhuwit.feature.overview.router

import androidx.fragment.app.Fragment
import id.dhuwit.feature.overview.ui.overview.OverviewFragment

object OverviewRouterImpl : OverviewRouter {
    override fun openDashboardPage(): Fragment {
        return OverviewFragment()
    }
}