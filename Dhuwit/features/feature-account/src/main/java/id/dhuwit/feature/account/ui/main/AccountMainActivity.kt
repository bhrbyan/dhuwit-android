package id.dhuwit.feature.account.ui.main

import android.content.res.Configuration
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.base.extension.gone
import id.dhuwit.core.base.extension.visible
import id.dhuwit.core.base.state.ViewState
import id.dhuwit.core.setting.user.SettingUser
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.uikit.databinding.EmptyStateBinding
import id.dhuwit.core.uikit.databinding.ToolbarBinding
import id.dhuwit.core.uikit.divider.UikitDividerMarginItemDecorationViewPager
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountMainActivityBinding
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.account.ui.main.adapter.main.AccountMainAdapter
import id.dhuwit.feature.account.ui.main.adapter.transaction.header.AccountMainTransactionHeaderAdapter
import id.dhuwit.feature.account.ui.main.adapter.transaction.item.AccountMainTransactionItemListener
import id.dhuwit.feature.transaction.router.TransactionRouter
import javax.inject.Inject

@AndroidEntryPoint
class AccountMainActivity : id.dhuwit.core.base.base.BaseActivity(),
    AccountMainTransactionItemListener {

    private lateinit var binding: AccountMainActivityBinding
    private lateinit var bindingToolbar: ToolbarBinding
    private lateinit var bindingEmptyState: EmptyStateBinding
    private lateinit var viewPagerCallback: ViewPager2.OnPageChangeCallback
    private lateinit var viewPagerAdapter: AccountMainAdapter
    private lateinit var adapterTransactionHeader: AccountMainTransactionHeaderAdapter

    private val viewModel: AccountMainViewModel by viewModels()

    @Inject
    lateinit var settingUser: SettingUser

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
            viewModel.getAccounts()
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
        viewModel.getAccounts()
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
            viewModel.onCreateTransaction()
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
                    openAccountFormPage(viewState.accountId)
                }
                is AccountMainViewState.GetTransactions -> {
                    binding.textIncomeAmount.text =
                        viewState.incomeAmount?.convertPriceWithCurrencyFormat(settingUser.getSymbolCurrency())
                    binding.textExpenseAmount.text =
                        viewState.expenseAmount?.convertPriceWithCurrencyFormat(settingUser.getSymbolCurrency())

                    adapterTransactionHeader.updateList(viewState.transactions)
                    if (viewState.transactions.isNullOrEmpty()) {
                        showEmptyState()
                    } else {
                        hideEmptyState()
                    }
                }
                is AccountMainViewState.CreateTransaction -> {
                    openTransactionPage(null, viewState.accountId)
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
        viewPagerAdapter = AccountMainAdapter(settingUser)

        binding.viewPager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = 1

            val layoutOrientation: Int = resources.configuration.orientation
            if (layoutOrientation == Configuration.ORIENTATION_PORTRAIT) {
                orientation = ViewPager2.ORIENTATION_HORIZONTAL

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
                val itemDecoration =
                    UikitDividerMarginItemDecorationViewPager(
                        context,
                        R.dimen.view_pager_current_item_horizontal_margin,
                        false
                    )
                addItemDecoration(itemDecoration)
            } else {
                orientation = ViewPager2.ORIENTATION_VERTICAL

                // Add a PageTransformer that translates the next and previous items vertically
                // towards the center of the screen, which makes them visible
                val nextItemVisiblePx = resources.getDimension(R.dimen.view_pager_next_item_visible)
                val currentItemVerticalMarginPx =
                    resources.getDimension(R.dimen.view_pager_current_item_vertical_margin)
                val pageTranslationY = nextItemVisiblePx + currentItemVerticalMarginPx
                val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
                    page.translationY = -pageTranslationY * position

                    // Next line scales the item's height. You can remove it if you don't want this effect
                    // page.scaleY = 1 - (0.25f * abs(position))

                    // If you want a fading effect uncomment the next line:
                    // page.alpha = 0.25f + (1 - abs(position))
                }
                setPageTransformer(pageTransformer)

                // The ItemDecoration gives the current (centered) item horizontal margin so that
                // it doesn't occupy the whole screen width. Without it the items overlap
                val itemDecoration =
                    UikitDividerMarginItemDecorationViewPager(
                        context,
                        R.dimen.view_pager_current_item_horizontal_margin,
                        true
                    )
                addItemDecoration(itemDecoration)
            }
        }
    }

    private fun setUpAdapterHeaderTransaction() {
        adapterTransactionHeader = AccountMainTransactionHeaderAdapter(settingUser).apply {
            listener = this@AccountMainActivity
        }
        binding.recyclerViewTransactions.apply {
            adapter = adapterTransactionHeader
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onClickTransaction(transaction: Transaction?) {
        openTransactionPage(transaction?.id, null)
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

    private fun openTransactionPage(transactionId: Long?, accountId: Long?) {
        addTransactionResult.launch(
            transactionRouter.openTransactionPage(
                this,
                transactionId,
                accountId
            )
        )
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