package id.dhuwit.feature.transaction

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.extension.*
import id.dhuwit.core.helper.DateHelper
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

    override fun init() {
        binding = TransactionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoadingGetTransaction()
        val transactionId = intent.getLongExtra(KEY_TRANSACTION_ID, DEFAULT_TRANSACTION_ID)
        viewModel.setUpTransaction(transactionId)

        if (transactionId == DEFAULT_TRANSACTION_ID) {
            setUpToolbar(getString(R.string.transaction_toolbar_title_add))
            disableButtonDelete()
        } else {
            setUpToolbar(getString(R.string.transaction_toolbar_title_update))
            enableButtonDelete()
        }
    }

    override fun listener() {
        with(binding) {
            buttonZero.setOnClickListener {
                viewModel.setCounter(getString(R.string.transaction_calculator_zero))
            }

            buttonOne.setOnClickListener {
                viewModel.setCounter(getString(R.string.transaction_calculator_one))
            }

            buttonTwo.setOnClickListener {
                viewModel.setCounter(getString(R.string.transaction_calculator_two))
            }

            buttonThree.setOnClickListener {
                viewModel.setCounter(getString(R.string.transaction_calculator_three))
            }

            buttonFour.setOnClickListener {
                viewModel.setCounter(getString(R.string.transaction_calculator_four))
            }

            buttonFive.setOnClickListener {
                viewModel.setCounter(getString(R.string.transaction_calculator_five))
            }

            buttonSix.setOnClickListener {
                viewModel.setCounter(getString(R.string.transaction_calculator_six))
            }

            buttonSeven.setOnClickListener {
                viewModel.setCounter(getString(R.string.transaction_calculator_seven))
            }

            buttonEight.setOnClickListener {
                viewModel.setCounter(getString(R.string.transaction_calculator_eight))
            }

            buttonNine.setOnClickListener {
                viewModel.setCounter(getString(R.string.transaction_calculator_nine))
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

            buttonDate.setOnClickListener {
                openDatePicker(viewModel.date.value?.convertToMillis(PATTERN_DATE_DATABASE))
            }

            buttonCategory.setOnClickListener {
                viewModel.onOpenCategory()
            }

            buttonNote.setOnClickListener {
                viewModel.onOpenNote()
            }

            buttonSave.setOnClickListener {
                viewModel.processTransaction()
            }

            buttonDelete.setOnClickListener {
                viewModel.deleteTransaction()
            }
        }
    }

    override fun observer() {
        with(viewModel) {
            amount.observe(this@TransactionActivity) { amount ->
                setTextAmount(amount)
                hideLoadingGetTransaction()
            }
            date.observe(this@TransactionActivity) { date -> setTextDate(date) }
            category.observe(this@TransactionActivity) { category -> setTextCategory(category) }
            note.observe(this@TransactionActivity) { note -> setTextNote(note) }
            transactionType.observe(this@TransactionActivity) { transactionType ->
                when (transactionType) {
                    TransactionType.Expense -> binding.layoutButtonToggle.check(binding.buttonToggleExpense.id)
                    TransactionType.Income -> binding.layoutButtonToggle.check(binding.buttonToggleIncome.id)
                }
            }
            openCategory.observe(this@TransactionActivity) { categoryType ->
                categoryType?.let { type ->
                    openCategoryPage(type)
                    viewModel.successOpenCategory()
                }
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

    private fun enableButtonDelete() {
        binding.buttonDelete.enabled()
        binding.buttonDelete.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_delete
            )
        )
    }

    private fun disableButtonDelete() {
        binding.buttonDelete.disabled()
        binding.buttonDelete.setImageDrawable(null)
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
        val convertedDate = date.convertPattern(PATTERN_DATE_DATABASE, PATTERN_DATE_TRANSACTION)
        binding.textDate.text = when (convertedDate) {
            DateHelper.getDate(
                PATTERN_DATE_TRANSACTION,
                COUNT_TODAY
            ) -> getString(R.string.transaction_date_today)
            DateHelper.getDate(
                PATTERN_DATE_TRANSACTION,
                COUNT_TOMORROW
            ) -> getString(R.string.transaction_date_tomorrow)
            DateHelper.getDate(
                PATTERN_DATE_TRANSACTION,
                COUNT_YESTERDAY
            ) -> getString(R.string.transaction_date_yesterday)
            else -> convertedDate
        }
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
        private const val COUNT_TODAY: Int = 0
        private const val COUNT_TOMORROW: Int = 1
        private const val COUNT_YESTERDAY: Int = -1
    }
}