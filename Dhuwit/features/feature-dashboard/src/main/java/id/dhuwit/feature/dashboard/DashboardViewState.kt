package id.dhuwit.feature.dashboard

import id.dhuwit.feature.dashboard.model.Dashboard
import id.dhuwit.state.ViewState

sealed class DashboardViewState : ViewState.Feature() {
    object TransactionNotFound : DashboardViewState()

    data class SetPeriodDate(val periodDate: String?) : DashboardViewState()

    data class GetDetails(val dashboard: Dashboard) : DashboardViewState()
}
