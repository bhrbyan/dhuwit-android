package id.dhuwit.feature.transaction.ui

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.extension.*
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_DATABASE
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_TRANSACTION
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.helper.DateHelper.convertToDate
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.account.AccountConstants.KEY_ACCOUNT_ID
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.category.router.CategoryRouter
import id.dhuwit.feature.category.ui.list.CategoryListConstants.KEY_SELECT_CATEGORY_ID
import id.dhuwit.feature.category.ui.list.CategoryListConstants.KEY_SELECT_CATEGORY_TYPE
import id.dhuwit.feature.note.NoteConstants.KEY_INPUT_NOTE
import id.dhuwit.feature.note.router.NoteRouter
import id.dhuwit.feature.transaction.R
import id.dhuwit.feature.transaction.databinding.TransactionActivityBinding
import id.dhuwit.feature.transaction.dialog.TransactionDeleteConfirmationListener
import id.dhuwit.feature.transaction.dialog.TransactionDeleteDialogFragment
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class TransactionActivity : BaseActivity(), TransactionDeleteConfirmationListener {

    private lateinit var binding: TransactionActivityBinding
    private val viewModel: TransactionViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var categoryRouter: CategoryRouter

    @Inject
    lateinit var noteRouter: NoteRouter

    @Inject
    lateinit var accountRouter: AccountRouter

    private val categoryResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val categoryId = result.data?.getLongExtra(KEY_SELECT_CATEGORY_ID, -1)
            val categoryType = result.data?.getStringExtra(KEY_SELECT_CATEGORY_TYPE)
            viewModel.updateCategory(CategoryType.getCategoryType(categoryType), categoryId)
        }
    }

    private val noteResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val note = result.data?.getStringExtra(KEY_INPUT_NOTE)
            viewModel.updateNote(note)
        }
    }

    private val accountResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val accountId = result.data?.getLongExtra(KEY_ACCOUNT_ID, 1) ?: 1
            viewModel.updateAccount(accountId)
        }

    }

    override fun init() {
        binding = TransactionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoadingGetTransaction()
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
                            viewModel.setType(TransactionType.Income)
                            viewModel.updateCategory(CategoryType.Income)
                        }
                        binding.buttonToggleExpense.id -> {
                            viewModel.setType(TransactionType.Expense)
                            viewModel.updateCategory(CategoryType.Expense)
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
                viewModel.onOpenDatePicker()
            }

            buttonCategory.setOnClickListener {
                viewModel.onOpenCategory()
            }

            buttonNote.setOnClickListener {
                viewModel.onOpenNotePage()
            }

            buttonSave.setOnClickListener {
                viewModel.processTransaction()
            }

            buttonDelete.setOnClickListener {
                showDialogDeleteConfirmation()
            }

            buttonAccount.setOnClickListener {
                openAccountPage()
            }
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this@TransactionActivity) {
            when (it) {
                is TransactionViewState.SetUpViewNewTransaction -> {
                    setUpToolbar(getString(R.string.transaction_toolbar_title_add))
                    hideButtonDelete()
                }
                is TransactionViewState.SetUpViewUpdateTransaction -> {
                    setUpToolbar(getString(R.string.transaction_toolbar_title_update))
                    showButtonDelete()
                }
                is TransactionViewState.SetUpTransaction -> {
                    setTextAmount(it.amount)
                    setTextDate(it.date)
                    setToggleTransactionType(it.type)
                    setTextNote(it.note)
                    setTextAccount(it.account)
                    setTextCategory(it.category)

                    hideLoadingGetTransaction()
                }
                is TransactionViewState.UpdateAmount -> {
                    setTextAmount(it.amount)
                }
                is TransactionViewState.UpdateCategory -> {
                    setTextCategory(it.category)
                }
                is TransactionViewState.UpdateAccount -> {
                    setTextAccount(it.account)
                }
                is TransactionViewState.UpdateDate -> {
                    setTextDate(it.date)
                }
                is TransactionViewState.UpdateNote -> {
                    setTextNote(it.note)
                }
                is TransactionViewState.OpenDatePicker -> {
                    openDatePicker(it.date)
                }
                is TransactionViewState.OpenNotePage -> {
                    openNotePage(it.note)
                }
                is TransactionViewState.OpenCategoryPage -> {
                    openCategoryPage(it.categoryType)
                }
                is TransactionViewState.SuccessSaveTransaction -> {
                    hideLoadingSaveTransaction()
                    setResult(RESULT_OK)
                    finish()
                }
                is TransactionViewState.ErrorAccountEmpty -> {
                    showError(getString(R.string.transaction_caption_account_error))
                }
                is ViewState.Error -> {
                    showError(getString(R.string.general_error_message))
                }
                else -> {
                    throw Exception("View State Not Found")
                }
            }
        }
    }

    private fun showButtonDelete() {
        binding.buttonDelete.visible()
    }

    private fun hideButtonDelete() {
        binding.buttonDelete.gone()
    }

    private fun openDatePicker(selectionDate: Long?) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.transaction_date_picker_title))
            .setSelection(selectionDate)
            .build()

        datePicker.apply {
            addOnPositiveButtonClickListener { dateInMillis ->
                viewModel.updateDate(dateInMillis.convertToDate(PATTERN_DATE_DATABASE))
            }

            show(supportFragmentManager, MaterialDatePicker::class.java.simpleName)
        }
    }

    private fun setTextNote(note: String?) {
        binding.textNote.text = if (note.isNullOrEmpty()) {
            getString(R.string.transaction_caption_note_empty)
        } else {
            note
        }
    }

    private fun setTextAccount(account: Account?) {
        binding.textAccount.apply {
            if (account != null) {
                text = account.name
                setTextColor(ContextCompat.getColor(context, R.color.colorOnMajor))
            } else {
                text = getString(R.string.transaction_caption_select_account)
                setTextColor(ContextCompat.getColor(context, R.color.colorError))
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

    private fun setTextDate(date: String?) {
        val convertedDate = date?.convertPattern(PATTERN_DATE_DATABASE, PATTERN_DATE_TRANSACTION)
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

    private fun setToggleTransactionType(type: TransactionType?) {
        when (type) {
            TransactionType.Expense -> binding.layoutButtonToggle.check(binding.buttonToggleExpense.id)
            TransactionType.Income -> binding.layoutButtonToggle.check(binding.buttonToggleIncome.id)
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
        binding.buttonSave.disabled()
    }

    private fun hideLoadingSaveTransaction() {
        binding.progressBarSave.hide()
        binding.buttonSave.enabled()
    }

    private fun openCategoryPage(categoryType: CategoryType?) {
        categoryResult.launch(categoryRouter.openCategoryListPage(this, categoryType.toString()))
    }

    private fun openNotePage(note: String?) {
        noteResult.launch(noteRouter.openNotePage(this, note))
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showDialogDeleteConfirmation() {
        TransactionDeleteDialogFragment().show(
            supportFragmentManager,
            TransactionDeleteDialogFragment::class.java.simpleName
        )
    }

    override fun onConfirmDeleteTransaction() {
        viewModel.deleteTransaction()
    }

    private fun openAccountPage() {
        accountResult.launch(
            accountRouter.openAccountSelectionPage(this)
        )
    }

    companion object {
        private const val DEFAULT_TRANSACTION_ID: Long = -1
        private const val COUNT_TODAY: Int = 0
        private const val COUNT_TOMORROW: Int = 1
        private const val COUNT_YESTERDAY: Int = -1
    }
}