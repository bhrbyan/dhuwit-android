package id.dhuwit.feature.account.ui.main

import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountMainActivityBinding
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.account.ui.main.adapter.AccountMainAdapter
import id.dhuwit.feature.account.ui.main.adapter.transaction.header.AccountMainTransactionHeaderAdapter
import id.dhuwit.feature.account.ui.main.adapter.transaction.item.AccountMainTransactionItemListener
import id.dhuwit.feature.transaction.router.TransactionRouter
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import id.dhuwit.uikit.databinding.EmptyStateBinding
import id.dhuwit.uikit.databinding.ToolbarBinding
import id.dhuwit.uikit.divider.DividerMarginItemDecorationViewPager
import javax.inject.Inject

@AndroidEntryPoint
class AccountMainActivity : BaseActivity(), AccountMainTransactionItemListener {

    private lateinit var binding: AccountMainActivityBinding
    private lateinit var bindingToolbar: ToolbarBinding
    private lateinit var bindingEmptyState: EmptyStateBinding
    private lateinit var viewPagerCallback: ViewPager2.OnPageChangeCallback
    private lateinit var viewPagerAdapter: AccountMainAdapter
    private lateinit var adapterTransactionHeader: AccountMainTransactionHeaderAdapter

    private val viewModel: AccountMainViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var accountRouter: AccountRouter

    @Inject
    lateinit var transactionRouter: TransactionRouter

    private val createAccountResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.getAccounts()
            }
        }

    private val addTransactionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.getTransactions()
        }
    }

    override fun init() {
        binding = AccountMainActivityBinding.inflate(layoutInflater)
        bindingToolbar = binding.layoutToolbar
        bindingEmptyState = binding.layoutEmptyState
        setContentView(binding.root)

        setUpToolbar()
        setUpViewPagerAdapter()
        setUpEmptyState()
        setUpAdapterHeaderTransaction()

        viewModel.setDefaultPeriodDate()
    }

    override fun listener() {
        viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.getTransactionsSelectedAccount(position)
            }
        }
        binding.viewPager.registerOnPageChangeCallback(viewPagerCallback)

        bindingToolbar.imageActionRight.setOnClickListener {
            // Set account id to null for create new account
            openAccountFormPage(null)
        }

        bindingToolbar.imageActionLeft.setOnClickListener {
            viewModel.onClickUpdateAccount()
        }

        binding.imageNext.setOnClickListener {
            viewModel.onNextPeriodDate()
        }

        binding.imagePrevious.setOnClickListener {
            viewModel.onPreviousPeriodDate()
        }

        binding.buttonAddTransaction.setOnClickListener {
            openTransactionPage(null)
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) { viewState ->
            when (viewState) {
                is AccountMainViewState.SetPeriodDate -> {
                    binding.textPeriodDate.text = viewState.periodDate
                }
                is AccountMainViewState.GetAccounts -> {
                    viewPagerAdapter.submitList(viewState.accounts)
                }
                is AccountMainViewState.UpdateAccount -> {
                    openAccountFormPage(viewState.accoundId)
                }
                is AccountMainViewState.GetTransactions -> {
                    binding.textIncomeAmount.text =
                        viewState.incomeAmount?.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
                    binding.textExpenseAmount.text =
                        viewState.expenseAmount?.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())

                    adapterTransactionHeader.updateList(viewState.transactions)
                    if (viewState.transactions.isNullOrEmpty()) {
                        showEmptyState()
                    } else {
                        hideEmptyState()
                    }
                }
                is ViewState.Error -> showError()
            }
        }
    }

    private fun setUpToolbar() {
        bindingToolbar.apply {
            textTitle.apply {
                text = getString(R.string.account_main_toolbar_title)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            imageActionRight.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_add_account_white
                    )
                )
                visible()
            }
            imageActionLeft.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_setting_white))
                visible()
            }
        }
    }

    private fun setUpViewPagerAdapter() {
        viewPagerAdapter = AccountMainAdapter(storage)
        binding.viewPager.apply {
            adapter = viewPagerAdapter

            offscreenPageLimit = 1

            // Add a PageTransformer that translates the next and previous items horizontally
            // towards the center of the screen, which makes them visible
            val nextItemVisiblePx = resources.getDimension(R.dimen.view_pager_next_item_visible)
            val currentItemHorizontalMarginPx =
                resources.getDimension(R.dimen.view_pager_current_item_horizontal_margin)
            val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
            val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
                page.translationX = -pageTranslationX * position

                // Next line scales the item's height. You can remove it if you don't want this effect
                // page.scaleY = 1 - (0.25f * abs(position))

                // If you want a fading effect uncomment the next line:
                // page.alpha = 0.25f + (1 - abs(position))
            }
            setPageTransformer(pageTransformer)

            // The ItemDecoration gives the current (centered) item horizontal margin so that
            // it doesn't occupy the whole screen width. Without it the items overlap
            val itemDecoration = DividerMarginItemDecorationViewPager(
                context,
                R.dimen.view_pager_current_item_horizontal_margin
            )
            addItemDecoration(itemDecoration)
        }
    }

    private fun setUpAdapterHeaderTransaction() {
        adapterTransactionHeader = AccountMainTransactionHeaderAdapter(storage).apply {
            listener = this@AccountMainActivity
        }
        binding.recyclerViewTransactions.apply {
            adapter = adapterTransactionHeader
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onClickTransaction(transaction: Transaction?) {
        openTransactionPage(transaction?.id)
    }

    private fun setUpEmptyState() {
        bindingEmptyState.apply {
            textTitle.text = getString(R.string.account_main_empty_state_transactions_title)
            textDescription.text =
                getString(R.string.account_main_empty_state_transactions_description)
        }
    }

    private fun showEmptyState() {
        bindingEmptyState.apply {
            textTitle.visible()
            textDescription.visible()
        }
    }

    private fun hideEmptyState() {
        bindingEmptyState.apply {
            textTitle.gone()
            textDescription.gone()
        }
    }

    private fun openAccountFormPage(accountId: Long?) {
        createAccountResult.launch(
            accountRouter.openAccountFormPage(this, accountId)
        )
    }

    private fun openTransactionPage(transactionId: Long?) {
        addTransactionResult.launch(transactionRouter.openTransactionPage(this, transactionId))
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(viewPagerCallback)
    }
}