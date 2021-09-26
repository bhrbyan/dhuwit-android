package id.dhuwit.feature.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val categoryRepository: CategoryDataSource
) : ViewModel() {

    private val _amount = MutableLiveData<Double?>()
    private val _amountCounter = MutableLiveData<String>()
    private val _date = MutableLiveData<String>()
    private val _category = MutableLiveData<Category>()
    private val _categories = MutableLiveData<List<Category>>()
    private val _note = MutableLiveData<String?>()
    private val _transactionType = MutableLiveData<TransactionType>()
    private val _openCategory = MutableLiveData<CategoryType>()
    private val _openNote = MutableLiveData<String?>()
    private val _processTransaction = MutableLiveData<State<Boolean>>()
    private val _transaction = MutableLiveData<State<Transaction>>()

    val amount: LiveData<Double?> = _amount
    val date: LiveData<String> = _date
    val category: LiveData<Category> = _category
    val note: LiveData<String?> = _note
    val transactionType: LiveData<TransactionType> = _transactionType
    val openCategory: LiveData<CategoryType> = _openCategory
    val openNote: LiveData<String?> = _openNote
    val processTransaction: LiveData<State<Boolean>> = _processTransaction
    val transaction: LiveData<State<Transaction>> = _transaction

    fun setUpTransaction() {
        viewModelScope.launch {
            _categories.value =
                categoryRepository.getCategories(CategoryType.Expense).data ?: emptyList()
            _categories.value?.first()?.let { setCategory(it) }

            setAmountCounter(DEFAULT_AMOUNT.convertDoubleToString())
            setTransactionDate(DateHelper.getCurrentDate(PATTERN_DATE_DATABASE))
            setTransactionType(TransactionType.Expense)
            setTransactionNote(null)
        }
    }

    private fun setTransactionNote(note: String?) {
        _note.value = note
    }

    private fun setCategory(category: Category) {
        _category.value = category
    }

    private fun setAmount(amount: Double?) {
        _amount.value = amount
    }

    fun setAmountCounter(counter: String) {
        if (isCounterLengthMoreThanOne() && isFirstCharZero()) {
            removeFirstChar()
        }

        var fullCounter: String = _amountCounter.value ?: String()
        fullCounter += counter

        if (isCounterLengthLessThanEqualHundredBillion(fullCounter)) {
            _amountCounter.value = fullCounter
            setAmount(_amountCounter.value?.toDouble())
        }
    }

    private fun isCounterLengthLessThanEqualHundredBillion(fullCounter: String): Boolean {
        return fullCounter.length <= 9
    }

    private fun isCounterLengthMoreThanOne(): Boolean {
        return _amountCounter.value?.length ?: 0 > 1
    }

    private fun isFirstCharZero(): Boolean {
        return _amountCounter.value?.first() == '0'
    }

    private fun removeFirstChar() {
        _amountCounter.value = _amountCounter.value?.removePrefix("0")
    }

    fun getTransaction(transactionId: Long) {
        _transaction.value = State.Loading()
        viewModelScope.launch {
            _transaction.value = transactionRepository.getTransaction(transactionId)

            _transaction.value?.data?.let { transaction ->
                val categoryType = when (transaction.type) {
                    is TransactionType.Expense -> CategoryType.Expense
                    is TransactionType.Income -> CategoryType.Income
                    else -> throw Exception("Transaction Type not found!")
                }
                _categories.value =
                    categoryRepository.getCategories(categoryType).data ?: emptyList()
                _categories.value
                    ?.find { category -> category.id == _transaction.value?.data?.categoryId }
                    ?.let { category -> setCategory(category) }

                setAmountCounter(transaction.amount.convertDoubleToString())
                setTransactionDate(transaction.date)
                setTransactionType(transaction.type)
                setTransactionNote(transaction.note)
            }
        }
    }

    fun setTransactionDate(date: String) {
        _date.value = date
    }

    fun onClearCounter() {
        val counter = _amountCounter.value?.dropLast(1)

        _amountCounter.value = counter ?: String()
        val amount = if (_amountCounter.value.isNullOrEmpty()) {
            DEFAULT_AMOUNT
        } else {
            _amountCounter.value?.toDouble()
        }
        setAmount(amount)
    }

    fun setTransactionType(transactionType: TransactionType) {
        _transactionType.value = transactionType
    }

    fun updateCategories(categoryType: CategoryType) {
        viewModelScope.launch {
            _categories.value = categoryRepository.getCategories(categoryType).data ?: emptyList()
            _categories.value?.first()?.let { setCategory(it) }
        }
    }

    fun onOpenCategory() {
        _openCategory.value = when (_transactionType.value) {
            is TransactionType.Income -> CategoryType.Income
            is TransactionType.Expense -> CategoryType.Expense
            null -> throw Exception("Transaction Type Null")
        }
    }

    fun onSelectCategory(categoryId: Long?) {
        val category = _categories.value?.find { it.id == categoryId }
        category?.let { setCategory(it) }
    }

    fun setNote(note: String?) {
        _note.value = note
    }

    fun onOpenNote() {
        _openNote.value = _note.value
    }

    fun saveTransaction() {
        _processTransaction.value = State.Loading()
        viewModelScope.launch {
            _processTransaction.value = transactionRepository.saveTransaction(mapTransaction())
        }
    }

    private fun mapTransaction(): Transaction {
        return Transaction(
            type = _transactionType.value ?: TransactionType.Expense,
            amount = _amount.value ?: DEFAULT_AMOUNT,
            categoryId = _category.value?.id ?: DEFAULT_CATEGORY_ID,
            categoryName = _category.value?.name ?: DEFAULT_CATEGORY_NAME,
            note = _note.value,
            date = _date.value ?: DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
            createdAt = DateHelper.getCurrentDate(PATTERN_DATE_DATABASE)
        )
    }

    fun updateTransaction() {
        _processTransaction.value = State.Loading()
        viewModelScope.launch {
            _processTransaction.value =
                transactionRepository.updateTransaction(mapUpdateTransaction())
        }
    }

    private fun mapUpdateTransaction(): Transaction {
        return Transaction(
            id = _transaction.value?.data?.id ?: 0,
            type = _transactionType.value ?: TransactionType.Expense,
            amount = _amount.value ?: DEFAULT_AMOUNT,
            categoryId = _category.value?.id ?: DEFAULT_CATEGORY_ID,
            categoryName = _category.value?.name ?: DEFAULT_CATEGORY_NAME,
            note = _note.value,
            date = _date.value ?: DateHelper.getCurrentDate(PATTERN_DATE_DATABASE),
            createdAt = DateHelper.getCurrentDate(PATTERN_DATE_DATABASE)
        )
    }

    fun deleteTransaction() {
        _processTransaction.value = State.Loading()
        viewModelScope.launch {
            _processTransaction.value =
                transactionRepository.deleteTransaction(_transaction.value?.data?.id ?: 0)
        }
    }

    companion object {
        private const val DEFAULT_AMOUNT: Double = 0.0
        private const val DEFAULT_CATEGORY_ID: Long = 0
        private const val DEFAULT_CATEGORY_NAME: String = "-"
    }

}