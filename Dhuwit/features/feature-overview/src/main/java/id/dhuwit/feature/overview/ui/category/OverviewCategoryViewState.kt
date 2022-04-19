package id.dhuwit.feature.overview.ui.category

import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.transaction.model.TransactionCategory
import id.dhuwit.state.ViewState

sealed class OverviewCategoryViewState : ViewState.Feature() {

    object CategoryNotFound : OverviewCategoryViewState()

    data class GetDetails(
        val categoryTransaction: List<TransactionCategory>?,
        val periodDate: String?,
        val categoryType: CategoryType
    ) : OverviewCategoryViewState()

    data class SetPeriodDate(val periodDate: String?) : OverviewCategoryViewState()

    data class SetCategoryType(val categoryType: CategoryType) : OverviewCategoryViewState()


}
