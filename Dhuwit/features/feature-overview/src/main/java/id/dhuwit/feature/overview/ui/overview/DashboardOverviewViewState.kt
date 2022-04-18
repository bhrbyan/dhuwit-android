package id.dhuwit.feature.overview.ui.overview

import id.dhuwit.feature.overview.model.Dashboard
import id.dhuwit.state.ViewState

sealed class DashboardOverviewViewState : ViewState.Feature() {

    object TransactionNotFound : DashboardOverviewViewState()

    data class SetPeriodDate(val periodDate: String?) : DashboardOverviewViewState()

    data class GetOverview(val dashboard: Dashboard) : DashboardOverviewViewState()
}
