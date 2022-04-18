package id.dhuwit.feature.overview.ui.transaction

import id.dhuwit.feature.overview.model.Dashboard
import id.dhuwit.state.ViewState

sealed class OverviewTransactionViewState : ViewState.Feature() {

    object TransactionNotFound : OverviewTransactionViewState()

    data class SetPeriodDate(val periodDate: String?) : OverviewTransactionViewState()

    data class GetOverview(val dashboard: Dashboard) : OverviewTransactionViewState()
}
