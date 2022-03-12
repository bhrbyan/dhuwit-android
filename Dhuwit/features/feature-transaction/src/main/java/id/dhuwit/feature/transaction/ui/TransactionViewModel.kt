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
import id.dhuwit.core.transaction.model.Transaction
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

    private var counterAmount: String = DEFAULT_AMOUNT.convertDoubleToString()

    private var amount: Double? = null

    private var _viewState = MutableLiveData<ViewState>()
    var viewState: LiveData<ViewState> = _viewState

    private fun updateViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    private var _categories: List<Category>? = null
    private var _accounts: List<Account>? = null

    private var _transaction: Transaction? = null
    private var _transactionId: Long =
        savedStateHandle.get<Long>(TransactionRouterImpl.KEY_TRANSACTION_ID)
            ?: DEFAULT_TRANSACTION_ID

    private val _date = MutableLiveData<String>()
    private val _category = MutableLiveData<Category?>()
    private val _note = MutableLiveData<String?>()
    private val _account = MutableLiveData<Account?>()
    private val _transactionType = MutableLiveData<TransactionType>()
    private val _openCategory = MutableLiveData<CategoryType?>()
    private val _processTransaction = MutableLiveData<State<Boolean>>()

    val date: LiveData<String> = _date
    val category: LiveData<Category?> = _category
    val note: LiveData<String?> = _note
    val account: LiveData<Account?> = _account
    val transactionType: LiveData<TransactionType> = _transactionType
    val openCategory: LiveData<CategoryType?> = _openCategory
    val processTransaction: LiveData<State<Boolean>> = _processTransaction

    init {
        setUpTransaction(_transactionId)
    }

    private fun setUpTransaction(transactionId: Long) {
        viewModelScope.launch {
            if (isCreateTransaction()) {
                updateViewState(TransactionViewState.SetUpViewNewTransaction)

                setCounter(counterAmount)
                setTransactionDate(DateHelper.getCurrentDate(PATTERN_DATE_DATABASE))
                setTransactionType(TransactionType.Expense)
                setTransactionNote(null)

                _categories = categoryRepository.getCategories(CategoryType.Expense).data
                setCategory(_categories?.first())

                _accounts = accountRepository.getAccounts().data
                val account = _accounts?.find { it.isPrimary } ?: _accounts?.first()
                setAccount(account)
            } else {
                updateViewState(TransactionViewState.SetUpViewUpdateTransaction)

                _transaction = transactionRepository.getTransaction(transactionId).data

                _transaction?.let { transaction ->
                    setCounter(transaction.amount.convertDoubleToString())
                    setTransactionDate(transaction.date)
                    setTransactionType(transaction.type)
                    setTransactionNote(transaction.note)

                    val categoryType = getCategoryType(transaction.type)
                    _categories = categoryRepository.getCategories(categoryType).data
                    val category = _categories?.find { category ->
                        category.id == transaction.category?.id
                    }
                    setCategory(category)

                    _accounts = accountRepository.getAccounts().data
                    val account = _accounts?.find {
                        it.id == _transaction?.accountId
                    }
                    setAccount(account)
                }
            }
        }
    }

    private fun isCreateTransaction(): Boolean {
        return _transactionId == DEFAULT_TRANSACTION_ID
    }

    private fun getCategoryType(transactionType: TransactionType): CategoryType {
        return when (transactionType) {
            is TransactionType.Expense -> CategoryType.Expense
            is TransactionType.Income -> CategoryType.Income
            else -> throw Exception("Transaction Type not found!")
        }
    }

    fun setCounter(counter: String) {
        var fullCounter: String = counterAmount
        fullCounter += counter

        if (isCounterLengthMoreThanOne(fullCounter) && isFirstCharZero(fullCounter)) {
            fullCounter = removeFirstChar(fullCounter)
        }

        if (isCounterLengthLessThanEqualHundredBillion(fullCounter)) {
            counterAmount = fullCounter
            amount = counterAmount.toDouble()
            updateViewState(
                TransactionViewState.SetAmount(amount)
            )
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

    fun setTransactionDate(date: String) {
        _date.value = date
    }


    private fun setTransactionNote(note: String?) {
        _note.value = note
    }

    private fun setCategory(category: Category?) {
        _category.value = category
    }

    private fun setAccount(account: Account?) {
        _account.value = account
    }

    fun onClearCounter() {
        val counter = counterAmount.dropLast(1)

        counterAmount = counter
        val amount = if (counter.isEmpty()) {
            DEFAULT_AMOUNT
        } else {
            counter.toDouble()
        }

        this.amount = amount
        updateViewState(
            TransactionViewState.SetAmount(amount)
        )
    }

    fun setTransactionType(transactionType: TransactionType) {
        _transactionType.value = transactionType
    }

    fun updateCategories(categoryType: CategoryType, categoryId: Long? = null) {
        viewModelScope.launch {
            _categories = categoryRepository.getCategories(categoryType).data
            val category = if (categoryId == null) {
                _categories?.first()
            } else {
                _categories?.find { it.id == categoryId }
            }

            setCategory(category)
        }
    }

    fun onOpenCategory() {
        _openCategory.value = when (_transactionType.value) {
            is TransactionType.Income -> CategoryType.Income
            is TransactionType.Expense -> CategoryType.Expense
            null -> throw Exception("Transaction Type Null")
        }
    }

    fun successOpenCategory() {
        _openCategory.value = null
    }

    fun setNote(note: String?) {
        _note.value = note
    }

    fun updateAccount(accountId: Long) {
        viewModelScope.launch {
            val account = accountRepository.getAccount(accountId).data
            setAccount(account)
        }
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
            transactionRepository.saveTransaction(mapTransaction())
            _processTransaction.value = accountRepository.updateBalance(
                _account.value?.id ?: 1,
                totalTransaction = amount ?: DEFAULT_AMOUNT,
                _transactionType.value == TransactionType.Expense
            )
        }
    }

    private fun updateTransaction() {
        viewModelScope.launch {
            transactionRepository.updateTransaction(mapTransaction())
            _processTransaction.value = accountRepository.updateBalance(
                accountId = _account.value?.id ?: 1,
                totalTransaction = amount ?: DEFAULT_AMOUNT,
                originalTotalTransaction = _transaction?.amount ?: 0.0,
                isExpenseTransaction = _transactionType.value == TransactionType.Expense
            )
        }
    }

    private fun mapTransaction(): Transaction {
        return if (isCreateTransaction()) {
            Transaction(
                type = _transactionType.value ?: TransactionType.Expense,
                amount = amount ?: DEFAULT_AMOUNT,
                note = _note.value,
                date = _date.value ?: DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                createdAt = DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                category = _category.value,
                accountId = _account.value?.id ?: 1
            )
        } else {
            Transaction(
                id = _transactionId,
                type = _transactionType.value ?: TransactionType.Expense,
                amount = amount ?: DEFAULT_AMOUNT,
                note = _note.value,
                date = _date.value ?: DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                createdAt = DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                category = _category.value,
                accountId = _account.value?.id ?: 1
            )
        }
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(_transactionId)

            _processTransaction.value = accountRepository.updateBalance(
                _account.value?.id ?: 1,
                _transactionType.value == TransactionType.Expense,
                _transaction?.amount ?: 0.0,
            )
        }
    }

    companion object {
        private const val DEFAULT_TRANSACTION_ID: Long = -1
        private const val DEFAULT_AMOUNT: Double = 0.0
        private const val DEFAULT_CATEGORY_ID: Long = 0
        private const val DEFAULT_CATEGORY_NAME: String = "-"
    }

}