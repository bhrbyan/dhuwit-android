package id.dhuwit.feature.transaction.ui

import id.dhuwit.core.account.model.Account
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.state.ViewState

sealed class TransactionViewState : ViewState.Feature() {

    object SetUpViewNewTransaction : TransactionViewState()

    object SetUpViewUpdateTransaction : TransactionViewState()

    object SuccessSaveTransaction : TransactionViewState()

    object ErrorAccountEmpty : TransactionViewState()

    data class SetUpTransaction(
        val amount: Double?,
        val date: String?,
        val type: TransactionType?,
        val note: String?,
        val account: Account?,
        val category: Category?
    ) : TransactionViewState()

    data class UpdateAmount(val amount: Double?) : TransactionViewState()

    data class UpdateAccount(val account: Account?) : TransactionViewState()

    data class UpdateDate(val date: String?) : TransactionViewState()

    data class UpdateNote(val note: String?) : TransactionViewState()

    data class UpdateCategory(val category: Category?) : TransactionViewState()

    data class OpenDatePicker(val date: Long?) : TransactionViewState()

    data class OpenNotePage(val note: String?) : TransactionViewState()

    data class OpenCategoryPage(val categoryType: CategoryType?) : TransactionViewState()

}