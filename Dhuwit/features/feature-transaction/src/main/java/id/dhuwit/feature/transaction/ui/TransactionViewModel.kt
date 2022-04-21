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
import id.dhuwit.core.transaction.model.TransactionGetType
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.core.transaction.repository.TransactionDataSource
import id.dhuwit.feature.transaction.router.TransactionRouterImpl
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

    private var amount: Double? = null
    private var date: String? = null
    private var type: TransactionType? = null
    private var note: String? = null
    private var accounts: List<Account>? = null
    private var account: Account? = null
    private var category: Category? = null
    private var transaction: Transaction? = null

    private var _viewState = MutableLiveData<ViewState>()
    var viewState: LiveData<ViewState> = _viewState

    init {
        setUpTransaction(transactionId)
    }

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    private fun setUpTransaction(transactionId: Long) {
        viewModelScope.launch {
            if (isCreateTransaction()) {
                updateViewState(TransactionViewState.SetUpViewNewTransaction)

                setCounter(counterAmount, false)
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

                updateViewState(
                    TransactionViewState.SetUpTransaction(
                        amount = amount,
                        date = date,
                        type = type,
                        note = note,
                        account = account,
                        category = category
                    )
                )
            } else {
                updateViewState(TransactionViewState.SetUpViewUpdateTransaction)

                transaction =
                    transactionRepository.getTransaction(TransactionGetType.GetById(transactionId)).data

                setCounter(transaction?.amount?.convertDoubleToString(), false)
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

                updateViewState(
                    TransactionViewState.SetUpTransaction(
                        amount = amount,
                        date = date,
                        type = type,
                        note = note,
                        account = account,
                        category = category
                    )
                )
            }
        }
    }

    private fun isCreateTransaction(): Boolean {
        return transactionId == DEFAULT_TRANSACTION_ID
    }

    private fun setAmount(amount: Double?) {
        this.amount = amount
    }

    private fun setDate(date: String?) {
        this.date = date
    }

    fun setType(type: TransactionType?) {
        this.type = type
    }

    private fun setNote(note: String?) {
        this.note = note
    }

    private fun setAccount(account: Account?) {
        this.account = account
    }

    private fun setCategory(category: Category?) {
        this.category = category
    }

    private fun getCategoryType(transactionType: TransactionType?): CategoryType {
        return when (transactionType) {
            is TransactionType.Expense -> CategoryType.Expense
            is TransactionType.Income -> CategoryType.Income
            else -> throw Exception("Transaction Type not found!")
        }
    }

    fun setCounter(counter: String?, sourceFromKeyboard: Boolean = true) {
        var fullCounter: String = counterAmount
        fullCounter += counter

        if (isCounterLengthMoreThanOne(fullCounter) && isFirstCharZero(fullCounter)) {
            fullCounter = removeFirstChar(fullCounter)
        }

        if (isCounterLengthLessThanEqualHundredBillion(fullCounter)) {
            counterAmount = fullCounter
            setAmount(counterAmount.toDouble())

            if (sourceFromKeyboard) {
                updateViewState(TransactionViewState.UpdateAmount(amount))
            }
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
        updateViewState(TransactionViewState.UpdateAmount(amount))
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
                    updateViewState(TransactionViewState.UpdateCategory(category))
                }
                is State.Error -> {
                    updateViewState(ViewState.Error(result.message))
                }
            }
        }
    }

    fun updateDate(date: String?) {
        setDate(date)
        updateViewState(TransactionViewState.UpdateDate(date))
    }

    fun updateNote(note: String?) {
        setNote(note)
        updateViewState(TransactionViewState.UpdateNote(note))
    }

    fun updateAccount(accountId: Long) {
        setAccount(accounts?.find { it.id == accountId })
        updateViewState(TransactionViewState.UpdateAccount(account))
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
                type = type ?: TransactionType.Expense,
                amount = amount ?: DEFAULT_AMOUNT,
                note = note,
                date = date ?: DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                createdAt = DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                category = category,
                accountId = account?.id ?: 1
            )
        } else {
            Transaction(
                id = transactionId,
                type = type ?: TransactionType.Expense,
                amount = amount ?: DEFAULT_AMOUNT,
                note = note,
                date = date ?: DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                createdAt = DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                category = category,
                accountId = account?.id ?: 1
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

    fun onOpenDatePicker() {
        updateViewState(
            TransactionViewState.OpenDatePicker(
                date?.convertToMillis(
                    PATTERN_DATE_DATABASE
                )
            )
        )
    }

    fun onOpenNotePage() {
        updateViewState(
            TransactionViewState.OpenNotePage(note)
        )
    }

    fun onOpenCategory() {
        updateViewState(
            TransactionViewState.OpenCategoryPage(getCategoryType(type))
        )
    }

    companion object {
        private const val DEFAULT_TRANSACTION_ID: Long = -1
        private const val DEFAULT_AMOUNT: Double = 0.0
    }

}