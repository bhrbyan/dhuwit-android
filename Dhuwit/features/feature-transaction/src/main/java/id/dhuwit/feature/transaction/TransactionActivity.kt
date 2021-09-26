package id.dhuwit.feature.transaction

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.enabled
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_DATABASE
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_TRANSACTION
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.helper.DateHelper.convertToDate
import id.dhuwit.core.helper.DateHelper.convertToMillis
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.category.CategoryListConstants.KEY_CATEGORY_ID
import id.dhuwit.feature.category.router.CategoryRouter
import id.dhuwit.feature.note.NoteConstants.KEY_INPUT_NOTE
import id.dhuwit.feature.note.router.NoteRouter
import id.dhuwit.feature.transaction.databinding.TransactionActivityBinding
import id.dhuwit.feature.transaction.router.TransactionRouterImpl.KEY_TRANSACTION_ID
import id.dhuwit.state.State
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class TransactionActivity : BaseActivity() {

    private lateinit var binding: TransactionActivityBinding
    private val viewModel: TransactionViewModel by viewModels()
    private var transactionId: Long = DEFAULT_TRANSACTION_ID

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var categoryRouter: CategoryRouter

    @Inject
    lateinit var noteRouter: NoteRouter

    private val categoryResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val categoryId = result.data?.getLongExtra(KEY_CATEGORY_ID, -1)
            viewModel.onSelectCategory(categoryId)
        }
    }

    private val noteResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val note = result.data?.getStringExtra(KEY_INPUT_NOTE)
            viewModel.setNote(note)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TransactionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionId = intent.getLongExtra(KEY_TRANSACTION_ID, DEFAULT_TRANSACTION_ID)
        if (transactionId == DEFAULT_TRANSACTION_ID) {
            setUpToolbar(getString(R.string.transaction_toolbar_title_add))
            viewModel.setUpTransaction()
        } else {
            setUpToolbar(getString(R.string.transaction_toolbar_title_update))
            setUpButtonDelete()
            viewModel.getTransaction(transactionId)
        }

        setListener()

        observer()
    }

    private fun setUpButtonDelete() {
        binding.buttonDelete.enabled()
        binding.buttonDelete.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_delete
            )
        )
    }

    private fun setListener() {
        with(binding) {
            buttonDate.setOnClickListener {
                openDatePicker(viewModel.date.value?.convertToMillis(PATTERN_DATE_DATABASE))
            }

            buttonZero.setOnClickListener {
                viewModel.setAmountCounter(getString(R.string.transaction_calculator_zero))
            }

            buttonOne.setOnClickListener {
                viewModel.setAmountCounter(getString(R.string.transaction_calculator_one))
            }

            buttonTwo.setOnClickListener {
                viewModel.setAmountCounter(getString(R.string.transaction_calculator_two))
            }

            buttonThree.setOnClickListener {
                viewModel.setAmountCounter(getString(R.string.transaction_calculator_three))
            }

            buttonFour.setOnClickListener {
                viewModel.setAmountCounter(getString(R.string.transaction_calculator_four))
            }

            buttonFive.setOnClickListener {
                viewModel.setAmountCounter(getString(R.string.transaction_calculator_five))
            }

            buttonSix.setOnClickListener {
                viewModel.setAmountCounter(getString(R.string.transaction_calculator_six))
            }

            buttonSeven.setOnClickListener {
                viewModel.setAmountCounter(getString(R.string.transaction_calculator_seven))
            }

            buttonEight.setOnClickListener {
                viewModel.setAmountCounter(getString(R.string.transaction_calculator_eight))
            }

            buttonNine.setOnClickListener {
                viewModel.setAmountCounter(getString(R.string.transaction_calculator_nine))
            }

            buttonClear.setOnClickListener {
                viewModel.onClearCounter()
            }

            layoutButtonToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (group.checkedButtonId) {
                        binding.buttonToggleIncome.id -> {
                            viewModel.setTransactionType(TransactionType.Income)
                            viewModel.updateCategories(CategoryType.Income)
                        }
                        binding.buttonToggleExpense.id -> {
                            viewModel.setTransactionType(TransactionType.Expense)
                            viewModel.updateCategories(CategoryType.Expense)
                        }
                    }
                } else {
                    // Make sure there are no un-selected button
                    if (group.checkedButtonId == -1) {
                        group.check(checkedId)
                    }
                }
            }

            buttonCategory.setOnClickListener {
                viewModel.onOpenCategory()
            }

            buttonNote.setOnClickListener {
                viewModel.onOpenNote()
            }

            buttonSave.setOnClickListener {
                if (transactionId == DEFAULT_TRANSACTION_ID) {
                    viewModel.saveTransaction()
                } else {
                    viewModel.updateTransaction()
                }
            }

            buttonDelete.setOnClickListener {
                viewModel.deleteTransaction()
            }
        }
    }

    private fun openDatePicker(selectionDate: Long?) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.transaction_date_picker_title))
            .setSelection(selectionDate)
            .build()

        datePicker.apply {
            addOnPositiveButtonClickListener { dateInMillis ->
                viewModel.setTransactionDate(dateInMillis.convertToDate(PATTERN_DATE_DATABASE))
            }

            show(supportFragmentManager, MaterialDatePicker::class.java.simpleName)
        }
    }

    override fun observer() {
        with(viewModel) {
            amount.observe(this@TransactionActivity) { amount ->
                setTextAmount(amount)
            }
            date.observe(this@TransactionActivity) { date ->
                setTextDate(date)
            }
            category.observe(this@TransactionActivity) { category ->
                setTextCategory(category)
            }
            note.observe(this@TransactionActivity) { note ->
                setTextNote(note)
            }
            transactionType.observe(this@TransactionActivity) { transactionType ->
                when (transactionType) {
                    TransactionType.Expense -> binding.layoutButtonToggle.check(binding.buttonToggleExpense.id)
                    TransactionType.Income -> binding.layoutButtonToggle.check(binding.buttonToggleIncome.id)
                }
            }
            openCategory.observe(this@TransactionActivity) { categoryType ->
                openCategoryPage(categoryType)
            }
            openNote.observe(this@TransactionActivity) { note ->
                openNotePage(note)
            }
            processTransaction.observe(this@TransactionActivity) {
                when (it) {
                    is State.Loading -> {
                        showLoadingSaveTransaction()
                    }
                    is State.Success -> {
                        hideLoadingSaveTransaction()
                        setResult(RESULT_OK)
                        finish()
                    }
                    is State.Error -> {
                        hideLoadingSaveTransaction()
                        showError()
                    }
                }
            }
        }
    }

    private fun setTextNote(note: String?) {
        with(binding) {
            if (note.isNullOrEmpty()) {
                textNote.gone()
                textCaptionNote.gone()
            } else {
                textCaptionNote.visible()
                textNote.apply {
                    visible()
                    text = note
                }
            }
        }
    }

    private fun setTextCategory(category: Category?) {
        binding.textCategory.text = category?.name
    }

    private fun setTextAmount(amount: Double?) {
        binding.textAmount.text = amount?.convertPriceWithCurrencyFormat(
            storage.getSymbolCurrency()
        )
    }

    private fun setTextDate(date: String) {
        binding.textDate.text = date.convertPattern(PATTERN_DATE_DATABASE, PATTERN_DATE_TRANSACTION)
    }

    private fun setUpToolbar(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun showLoadingGetTransaction() {
        binding.progressBarGet.show()
        binding.textAmount.gone()
    }

    private fun hideLoadingGetTransaction() {
        binding.progressBarGet.hide()
        binding.textAmount.visible()
    }

    private fun showLoadingSaveTransaction() {
        binding.progressBarSave.show()
        binding.buttonSave.gone()
    }

    private fun hideLoadingSaveTransaction() {
        binding.progressBarSave.hide()
        binding.buttonSave.visible()
    }

    private fun openCategoryPage(categoryType: CategoryType) {
        categoryResult.launch(categoryRouter.openCategoryListPage(this, categoryType.toString()))
    }

    private fun openNotePage(note: String?) {
        noteResult.launch(noteRouter.openNotePage(this, note))
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val DEFAULT_TRANSACTION_ID: Long = -1
    }
}