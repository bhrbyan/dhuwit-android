package id.dhuwit.feature.overview.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.transaction.model.TransactionCategory
import id.dhuwit.core.transaction.repository.TransactionDataSource
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewCategoryViewModel @Inject constructor(
    private val transactionRepository: TransactionDataSource
) : ViewModel() {

    private var periodMonth: Int = CURRENT_MONTH
    private var periodDate: String? = ""
    private var categoryType: CategoryType = CategoryType.Expense

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun getTransactions(periodDate: String?, categoryType: CategoryType?) {
        val date = periodDate ?: this@OverviewCategoryViewModel.periodDate
        val type = categoryType ?: this@OverviewCategoryViewModel.categoryType

        viewModelScope.launch {
            when (val result = transactionRepository.getCategoryTransaction(date, type)) {
                is State.Success -> {
                    setUpListCategories(result.data)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    private fun setUpListCategories(transactions: List<TransactionCategory>?) {
        if (transactions != null) {
            updateViewState(
                OverviewCategoryViewState.GetDetails(transactions, periodDate, categoryType)
            )
        } else {
            updateViewState(OverviewCategoryViewState.CategoryNotFound)
        }
    }

    fun onNextPeriodDate() {
        setPeriodDate(++periodMonth)
        getTransactions(periodDate, categoryType)
    }

    fun onPreviousPeriodDate() {
        setPeriodDate(--periodMonth)
        getTransactions(periodDate, categoryType)
    }

    fun setPeriodDate(periodDate: Int?) {
        val date = periodDate ?: this.periodMonth
        this.periodDate = DateHelper.getPeriodDate(date, DateHelper.PATTERN_DATE_PERIOD)
    }

    fun setCategoryType(categoryType: CategoryType) {
        this.categoryType = categoryType
        getTransactions(periodDate, categoryType)
    }

    fun openTransactionListPage(item: TransactionCategory) {
        updateViewState(
            OverviewCategoryViewState.OpenTransactionListPage(
                periodDate,
                item.categoryId
            )
        )
    }

    companion object {
        private const val CURRENT_MONTH: Int = 0
    }

}