package id.dhuwit.feature.dashboard.ui.overview

import id.dhuwit.feature.dashboard.model.Dashboard
import id.dhuwit.state.ViewState

sealed class DashboardOverviewViewState : ViewState.Feature() {

    object TransactionNotFound : DashboardOverviewViewState()

    data class SetPeriodDate(val periodDate: String?) : DashboardOverviewViewState()

    data class GetOverview(val dashboard: Dashboard) : DashboardOverviewViewState()
}
