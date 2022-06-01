package id.dhuwit.feature.transaction.ui

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_DATABASE
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_TRANSACTION
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.helper.DateHelper.convertToDate
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.calculator.databinding.CalculatorBottomSheetBinding
import id.dhuwit.feature.calculator.router.CalculatorRouter
import id.dhuwit.feature.calculator.ui.CalculatorListener
import id.dhuwit.feature.category.CategoryListConstants.KEY_SELECT_CATEGORY_ID
import id.dhuwit.feature.category.CategoryListConstants.KEY_SELECT_CATEGORY_TYPE
import id.dhuwit.feature.category.router.CategoryRouter
import id.dhuwit.feature.note.NoteConstants.KEY_INPUT_NOTE
import id.dhuwit.feature.note.router.NoteRouter
import id.dhuwit.feature.transaction.R
import id.dhuwit.feature.transaction.databinding.TransactionActivityBinding
import id.dhuwit.feature.transaction.dialog.TransactionDeleteConfirmationListener
import id.dhuwit.feature.transaction.dialog.TransactionDeleteDialogFragment
import id.dhuwit.feature.transaction.router.TransactionRouterImpl
import id.dhuwit.feature.transaction.ui.TransactionConstants.DEFAULT_TRANSACTION_ID
import id.dhuwit.feature.transaction.ui.account.TransactionAccountActivity
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import id.dhuwit.uikit.databinding.ToolbarBinding
import javax.inject.Inject

@AndroidEntryPoint
class TransactionActivity : BaseActivity(), TransactionDeleteConfirmationListener,
    CalculatorListener {

    private lateinit var binding: TransactionActivityBinding
    private lateinit var bindingToolbar: ToolbarBinding
    private lateinit var bindingCalculator: CalculatorBottomSheetBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: TransactionViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var categoryRouter: CategoryRouter

    @Inject
    lateinit var noteRouter: NoteRouter

    @Inject
    lateinit var calculatorRouter: CalculatorRouter

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
            val accountId = result.data?.getLongExtra(TransactionConstants.KEY_ACCOUNT_ID, 1) ?: 1
            viewModel.updateAccount(accountId)
        }
    }

    override fun init() {
        binding = TransactionActivityBinding.inflate(layoutInflater)
        bindingToolbar = binding.layoutToolbar
        bindingCalculator = binding.layoutCalculator
        setContentView(binding.root)

        val transactionId: Long =
            intent.getLongExtra(TransactionRouterImpl.KEY_TRANSACTION_ID, DEFAULT_TRANSACTION_ID)
        if (isCreateTransaction(transactionId)) {
            setUpToolbar(getString(R.string.transaction_toolbar_title_add))
            hideButtonDelete()
        } else {
            setUpToolbar(getString(R.string.transaction_toolbar_title_update))
            showButtonDelete()
        }

        setUpBottomSheet()
    }

    override fun listener() {
        with(binding) {
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
                viewModel.onOpenDatePicker(true)
            }

            buttonCategory.setOnClickListener {
                viewModel.onOpenCategory(true)
            }

            buttonNote.setOnClickListener {
                viewModel.onOpenNotePage(true)
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

            textAmount.setOnClickListener {
                showCalculator()
            }
        }
    }

    override fun observer() {
        viewModel.amount.observe(this) {
            setTextAmount(it)
        }

        viewModel.type.observe(this) {
            setToggleTransactionType(it)
        }

        viewModel.account.observe(this) {
            setTextAccount(it)
        }

        viewModel.category.observe(this) {
            setTextCategory(it)
        }

        viewModel.date.observe(this) {
            setTextDate(it)
        }

        viewModel.note.observe(this) {
            setTextNote(it)
        }

        viewModel.viewState.observe(this@TransactionActivity) {
            when (it) {
                is TransactionViewState.SuccessSaveTransaction -> {
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

        viewModel.navigationViewState.observe(this) {
            when (it) {
                is TransactionNavigationViewState.OpenDatePicker -> {
                    if (it.isOpen == true) {
                        openDatePicker(it.date)
                        viewModel.onOpenDatePicker(null)
                    }
                }
                is TransactionNavigationViewState.OpenNotePage -> {
                    if (it.isOpen == true) {
                        openNotePage(it.note)
                        viewModel.onOpenNotePage(null)
                    }
                }
                is TransactionNavigationViewState.OpenCategoryPage -> {
                    if (it.isOpen == true) {
                        openCategoryPage(it.categoryType)
                        viewModel.onOpenCategory(null)
                    }
                }
            }
        }
    }

    private fun isCreateTransaction(transactionId: Long): Boolean {
        return transactionId == DEFAULT_TRANSACTION_ID
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
        bindingToolbar.apply {
            textTitle.text = title
            imageActionLeft.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close))
                setOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                visible()
            }
        }
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
            Intent(this, TransactionAccountActivity::class.java)
        )
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(bindingCalculator.layoutBottomSheetRoot).apply {
                isHideable = true
            }

        // Default State
        showCalculator()

        supportFragmentManager.beginTransaction()
            .replace(bindingCalculator.frameLayout.id, calculatorRouter.getCalculatorFragment())
            .commit()
    }

    override fun onInputNumber(text: String) {
        viewModel.setCounter(text)
    }

    override fun onClear() {
        viewModel.onClearCounter()
    }

    override fun onClose() {
        closeCalculator()
    }

    private fun showCalculator() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun closeCalculator() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            closeCalculator()
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    companion object {
        private const val COUNT_TODAY: Int = 0
        private const val COUNT_TOMORROW: Int = 1
        private const val COUNT_YESTERDAY: Int = -1
    }
}