package id.dhuwit.feature.overview.ui.account

import id.dhuwit.core.transaction.model.TransactionAccount
import id.dhuwit.state.ViewState

sealed class OverviewAccountViewState : ViewState.Feature() {

    object CategoryNotFound : OverviewAccountViewState()

    data class GetDetails(
        val accountTransaction: List<TransactionAccount>?,
        val periodDate: String?
    ) : OverviewAccountViewState()

    data class SetPeriodDate(val periodDate: String?) : OverviewAccountViewState()

    data class OpenTransactionListPage(val periodDate: String?, val accountId: Long) :
        OverviewAccountViewState()

}
