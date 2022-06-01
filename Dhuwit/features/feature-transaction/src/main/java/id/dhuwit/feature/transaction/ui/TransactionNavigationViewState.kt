package id.dhuwit.feature.transaction.ui

import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.state.ViewState

sealed class TransactionNavigationViewState : ViewState.Feature() {

    data class OpenDatePicker(val date: Long?, val isOpen: Boolean?) :
        TransactionNavigationViewState()

    data class OpenNotePage(val note: String?, val isOpen: Boolean?) :
        TransactionNavigationViewState()

    data class OpenCategoryPage(val categoryType: CategoryType?, val isOpen: Boolean?) :
        TransactionNavigationViewState()

}