package id.dhuwit.feature.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import id.dhuwit.state.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionDataSource,
    private val categoryRepository: CategoryDataSource,
    private val accountRepository: AccountDataSource
) : ViewModel() {

    private var _categories: List<Category>? = null
    private var _counter: String = DEFAULT_AMOUNT.convertDoubleToString()
    private var _transactionId: Long = DEFAULT_TRANSACTION_ID
    private var _transaction: Transaction? = null

    private val _amount = MutableLiveData<Double?>()
    private val _date = MutableLiveData<String>()
    private val _category = MutableLiveData<Category?>()
    private val _note = MutableLiveData<String?>()
    private val _transactionType = MutableLiveData<TransactionType>()
    private val _openCategory = MutableLiveData<CategoryType?>()
    private val _openNote = MutableLiveData<Boolean?>()
    private val _processTransaction = MutableLiveData<State<Boolean>>()

    val amount: LiveData<Double?> = _amount
    val date: LiveData<String> = _date
    val category: LiveData<Category?> = _category
    val note: LiveData<String?> = _note
    val transactionType: LiveData<TransactionType> = _transactionType
    val openCategory: LiveData<CategoryType?> = _openCategory
    val openNote: LiveData<Boolean?> = _openNote
    val processTransaction: LiveData<State<Boolean>> = _processTransaction

    fun setUpTransaction(transactionId: Long) {
        setTransactionId(transactionId)
        viewModelScope.launch {
            if (isCreateTransaction()) {
                _categories = categoryRepository.getCategories(CategoryType.Expense).data

                setCounter(_counter)
                setTransactionDate(DateHelper.getCurrentDate(PATTERN_DATE_DATABASE))
                setTransactionType(TransactionType.Expense)
                setTransactionNote(null)
                setCategory(_categories?.first())
            } else {
                _transaction = transactionRepository.getTransaction(transactionId).data

                _transaction?.let { transaction ->
                    val categoryType = getCategoryType(transaction.type)
                    _categories = categoryRepository.getCategories(categoryType).data
                    val category =
                        _categories?.find { category -> category.id == transaction.category?.id }

                    setCounter(transaction.amount.convertDoubleToString())
                    setTransactionDate(transaction.date)
                    setTransactionType(transaction.type)
                    setTransactionNote(transaction.note)
                    setCategory(category)
                }
            }
        }
    }

    private fun setTransactionId(transactionId: Long) {
        _transactionId = transactionId
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
        if (isCounterLengthMoreThanOne() && isFirstCharZero()) {
            removeFirstChar()
        }

        var fullCounter: String = _counter
        fullCounter += counter

        if (isCounterLengthLessThanEqualHundredBillion(fullCounter)) {
            _counter = fullCounter
            setAmount(_counter.toDouble())
        }
    }

    private fun isCounterLengthLessThanEqualHundredBillion(fullCounter: String): Boolean {
        return fullCounter.length <= 9
    }

    private fun isFirstCharZero(): Boolean {
        return _counter.first() == '0'
    }

    private fun isCounterLengthMoreThanOne(): Boolean {
        return _counter.length > 1
    }

    private fun removeFirstChar() {
        _counter = _counter.removePrefix("0")
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

    private fun setAmount(amount: Double?) {
        _amount.value = amount
    }

    fun onClearCounter() {
        val counter = _counter.dropLast(1)

        _counter = counter
        val amount = if (counter.isEmpty()) {
            DEFAULT_AMOUNT
        } else {
            counter.toDouble()
        }
        setAmount(amount)
    }

    fun setTransactionType(transactionType: TransactionType) {
        _transactionType.value = transactionType
    }

    fun updateCategories(categoryType: CategoryType) {
        viewModelScope.launch {
            _categories = categoryRepository.getCategories(categoryType).data
            val category = _categories?.first()

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

    fun onSelectCategory(categoryId: Long?) {
        val category = _categories?.find { it.id == categoryId }
        setCategory(category)
    }

    fun setNote(note: String?) {
        _note.value = note
    }

    fun onOpenNote() {
        _openNote.value = true
    }

    fun processTransaction() {
        if (isCreateTransaction()) {
            saveTransaction()
        } else {
            updateTransaction()
        }
    }

    private fun saveTransaction() {
        _processTransaction.value = State.Loading()
        viewModelScope.launch {
            transactionRepository.saveTransaction(mapTransaction())
            _processTransaction.value = accountRepository.updateBalance(
                _amount.value ?: DEFAULT_AMOUNT,
                _transactionType.value == TransactionType.Expense
            )
        }
    }

    private fun updateTransaction() {
        _processTransaction.value = State.Loading()
        viewModelScope.launch {
            transactionRepository.updateTransaction(mapTransaction())
            _processTransaction.value = accountRepository.updateBalance(
                totalTransaction = _amount.value ?: DEFAULT_AMOUNT,
                originalTotalTransaction = _transaction?.amount ?: 0.0,
                isExpenseTransaction = _transactionType.value == TransactionType.Expense
            )
        }
    }

    private fun mapTransaction(): Transaction {
        return if (isCreateTransaction()) {
            Transaction(
                type = _transactionType.value ?: TransactionType.Expense,
                amount = _amount.value ?: DEFAULT_AMOUNT,
                note = _note.value,
                date = _date.value ?: DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                createdAt = DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                category = _category.value
            )
        } else {
            Transaction(
                id = _transactionId,
                type = _transactionType.value ?: TransactionType.Expense,
                amount = _amount.value ?: DEFAULT_AMOUNT,
                note = _note.value,
                date = _date.value ?: DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                createdAt = DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
                category = _category.value
            )
        }
    }

    fun deleteTransaction() {
        _processTransaction.value = State.Loading()
        viewModelScope.launch {
            transactionRepository.deleteTransaction(_transactionId)

            _processTransaction.value = accountRepository.updateBalance(
                _transactionType.value == TransactionType.Expense,
                _transaction?.amount ?: 0.0,
            )
        }
    }

    fun successOpenNote() {
        _openNote.value = null
    }

    companion object {
        private const val DEFAULT_TRANSACTION_ID: Long = -1
        private const val DEFAULT_AMOUNT: Double = 0.0
        private const val DEFAULT_CATEGORY_ID: Long = 0
        private const val DEFAULT_CATEGORY_NAME: String = "-"
    }

}