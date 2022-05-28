package id.dhuwit.feature.transaction.ui

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.category.repository.CategoryDataSource
import id.dhuwit.core.extension.convertDoubleToString
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_DATABASE
import id.dhuwit.core.helper.DateHelper.convertToMillis
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.core.transaction.repository.TransactionDataSource
import id.dhuwit.feature.transaction.router.TransactionRouterImpl
import id.dhuwit.feature.transaction.ui.TransactionConstants.DEFAULT_TRANSACTION_ID
import id.dhuwit.state.State
import id.dhuwit.state.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionDataSource,
    private val categoryRepository: CategoryDataSource,
    private val accountRepository: AccountDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var transactionId: Long =
        savedStateHandle.get<Long>(TransactionRouterImpl.KEY_TRANSACTION_ID)
            ?: DEFAULT_TRANSACTION_ID

    private var counterAmount: String = DEFAULT_AMOUNT.convertDoubleToString()

    private var accounts: List<Account>? = null
    private var transaction: Transaction? = null

    private var _viewState = MutableLiveData<ViewState>()
    var viewState: LiveData<ViewState> = _viewState

    private var _amount = MutableLiveData<Double?>()
    var amount: LiveData<Double?> = _amount

    private var _type = MutableLiveData<TransactionType?>()
    var type: LiveData<TransactionType?> = _type

    private var _account = MutableLiveData<Account?>()
    var account: LiveData<Account?> = _account

    private var _category = MutableLiveData<Category?>()
    var category: LiveData<Category?> = _category

    private var _date = MutableLiveData<String?>()
    var date: LiveData<String?> = _date

    private var _note = MutableLiveData<String?>()
    var note: LiveData<String?> = _note

    private var _navigationViewState = MutableLiveData<ViewState>()
    var navigationViewState: LiveData<ViewState> = _navigationViewState

    init {
        setUpTransaction(transactionId)
    }

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    private fun updateNavigationViewState(navigationViewState: ViewState) {
        _navigationViewState.value = navigationViewState
    }

    private fun setUpTransaction(transactionId: Long) {
        viewModelScope.launch {
            if (isCreateTransaction()) {
                setCounter(counterAmount)
                setDate(DateHelper.getCurrentDate(PATTERN_DATE_DATABASE))
                setType(TransactionType.Expense)
                setNote(null)

                when (val result = accountRepository.getAccounts()) {
                    is State.Success -> {
                        accounts = result.data
                        setAccount(
                            accounts?.find { it.isPrimary } ?: accounts?.first()
                        )
                    }
                    is State.Error -> {
                        updateViewState(ViewState.Error(result.message))
                    }
                }

                when (val result = categoryRepository.getCategories(CategoryType.Expense)) {
                    is State.Success -> {
                        setCategory(result.data?.first())
                    }
                    is State.Error -> {
                        updateViewState(ViewState.Error(result.message))
                    }
                }
            } else {
                transaction = transactionRepository.getTransaction(transactionId).data

                setCounter(transaction?.amount?.convertDoubleToString())
                setDate(transaction?.date)
                setType(transaction?.type)
                setNote(transaction?.note)

                when (val result = accountRepository.getAccounts()) {
                    is State.Success -> {
                        accounts = result.data

                        setAccount(
                            accounts?.find { it.id == transaction?.accountId }
                        )
                    }
                    is State.Error -> {
                        updateViewState(ViewState.Error(result.message))
                    }
                }

                val categoryType = getCategoryType(transaction?.type)
                when (val result = categoryRepository.getCategories(categoryType)) {
                    is State.Success -> {
                        setCategory(result.data?.find {
                            it.id == transaction?.category?.id
                        })
                    }
                    is State.Error -> {
                        updateViewState(ViewState.Error(result.message))
                    }
                }
            }
        }
    }

    private fun isCreateTransaction(): Boolean {
        return transactionId == DEFAULT_TRANSACTION_ID
    }

    private fun setAmount(amount: Double?) {
        _amount.value = amount
    }

    private fun setDate(date: String?) {
        _date.value = date
    }

    fun setType(type: TransactionType?) {
        _type.value = type
    }

    private fun setNote(note: String?) {
        _note.value = note
    }

    private fun setAccount(account: Account?) {
        _account.value = account
    }

    private fun setCategory(category: Category?) {
        _category.value = category
    }

    private fun getCategoryType(transactionType: TransactionType?): CategoryType {
        return when (transactionType) {
            is TransactionType.Expense -> CategoryType.Expense
            is TransactionType.Income -> CategoryType.Income
            else -> throw Exception("Transaction Type not found!")
        }
    }

    fun setCounter(counter: String?) {
        var fullCounter: String = counterAmount
        fullCounter += counter

        if (isCounterLengthMoreThanOne(fullCounter) && isFirstCharZero(fullCounter)) {
            fullCounter = removeFirstChar(fullCounter)
        }

        if (isCounterLengthLessThanEqualHundredBillion(fullCounter)) {
            counterAmount = fullCounter
            setAmount(counterAmount.toDouble())
        }
    }

    private fun isCounterLengthLessThanEqualHundredBillion(fullCounter: String): Boolean {
        return fullCounter.length <= 9
    }

    private fun isFirstCharZero(counter: String): Boolean {
        return counter.first() == '0'
    }

    private fun isCounterLengthMoreThanOne(counter: String): Boolean {
        return counter.length > 1
    }

    private fun removeFirstChar(counter: String): String {
        return counter.removePrefix("0")
    }

    fun onClearCounter() {
        val counter = counterAmount.dropLast(1)

        counterAmount = counter
        val finalAmount = if (counter.isEmpty()) {
            DEFAULT_AMOUNT
        } else {
            counter.toDouble()
        }

        setAmount(finalAmount)
    }

    /* There are 2 reason why need to fetch categories from db
    * 1. When user change transactionType need to update the categories
    * 2. When user select category, there's a chance user add new category
    * */
    fun updateCategory(categoryType: CategoryType, categoryId: Long? = null) {
        viewModelScope.launch {
            when (val result = categoryRepository.getCategories(categoryType)) {
                is State.Success -> {
                    val category = if (categoryId == null) {
                        result.data?.first()
                    } else {
                        result.data?.find { it.id == categoryId }
                    }

                    setCategory(category)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun updateDate(date: String?) {
        setDate(date)
    }

    fun updateNote(note: String?) {
        setNote(note)
    }

    fun updateAccount(accountId: Long) {
        setAccount(accounts?.find { it.id == accountId })
    }

    fun processTransaction() {
        if (isCreateTransaction()) {
            saveTransaction()
        } else {
            updateTransaction()
        }
    }

    private fun saveTransaction() {
        viewModelScope.launch {
            when (val result = transactionRepository.saveTransaction(mapTransaction())) {
                is State.Success -> {
                    updateViewState(
                        TransactionViewState.SuccessSaveTransaction
                    )
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    private fun updateTransaction() {
        if (account == null) {
            updateViewState(TransactionViewState.ErrorAccountEmpty)
            return
        }

        viewModelScope.launch {
            when (val result =
                transactionRepository.updateTransaction(mapTransaction(), transaction)) {
                is State.Success -> {
                    updateViewState(TransactionViewState.SuccessSaveTransaction)
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    private fun mapTransaction(): Transaction {
        return if (isCreateTransaction()) {
            Transaction(
                type = type.value ?: TransactionType.Expense,
                amount = amount.value ?: DEFAULT_AMOUNT,
                note = note.value,
                date = date.value ?: DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                createdAt = DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                category = category.value,
                accountId = account.value?.id ?: 1
            )
        } else {
            Transaction(
                id = transactionId,
                type = type.value ?: TransactionType.Expense,
                amount = amount.value ?: DEFAULT_AMOUNT,
                note = note.value,
                date = date.value ?: DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                createdAt = DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                category = category.value,
                accountId = account.value?.id ?: 1
            )
        }
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            when (val result = transactionRepository.deleteTransaction(transactionId)) {
                is State.Success -> {
                    updateViewState(TransactionViewState.SuccessSaveTransaction)
                }
                is State.Error -> {
                    updateViewState(
                        ViewState.Error(result.message)
                    )
                }
            }
        }
    }

    fun onOpenDatePicker(isOpen: Boolean?) {
        updateNavigationViewState(
            TransactionNavigationViewState.OpenDatePicker(
                date.value?.convertToMillis(
                    PATTERN_DATE_DATABASE
                ),
                isOpen
            )
        )
    }

    fun onOpenNotePage(isOpen: Boolean?) {
        updateNavigationViewState(
            TransactionNavigationViewState.OpenNotePage(note.value, isOpen)
        )
    }

    fun onOpenCategory(isOpen: Boolean?) {
        updateNavigationViewState(
            TransactionNavigationViewState.OpenCategoryPage(getCategoryType(type.value), isOpen)
        )
    }

    companion object {
        private const val DEFAULT_AMOUNT: Double = 0.0
    }

}