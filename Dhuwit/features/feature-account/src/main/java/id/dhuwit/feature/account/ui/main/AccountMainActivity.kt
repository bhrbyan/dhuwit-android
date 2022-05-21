package id.dhuwit.feature.account.ui.main

import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountMainActivityBinding
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.account.ui.main.adapter.AccountMainAdapter
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import id.dhuwit.uikit.databinding.EmptyStateBinding
import id.dhuwit.uikit.databinding.ToolbarBinding
import id.dhuwit.uikit.divider.DividerMarginItemDecorationViewPager
import javax.inject.Inject

@AndroidEntryPoint
class AccountMainActivity : BaseActivity() {

    private lateinit var binding: AccountMainActivityBinding
    private lateinit var bindingToolbar: ToolbarBinding
    private lateinit var bindingEmptyState: EmptyStateBinding
    private lateinit var viewPagerCallback: ViewPager2.OnPageChangeCallback
    private lateinit var viewPagerAdapter: AccountMainAdapter

    private val viewModel: AccountMainViewModel by viewModels()

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var accountRouter: AccountRouter

    private val createAccountResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
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

        viewModel.setDefaultPeriodDate()
    }

    override fun listener() {
        viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.getDetailSelectedAccount(position)
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
    }

    override fun observer() {
        viewModel.viewState.observe(this) { viewState ->
            when (viewState) {
                is AccountMainViewState.SetPeriodDate -> {
                    binding.textPeriodDate.text = viewState.periodDate
                }
                is AccountMainViewState.GetAccounts -> {
                    if (viewState.accounts.isNullOrEmpty()) {
                        showEmptyState()
                    } else {
                        hideEmptyState()
                        viewPagerAdapter.submitList(viewState.accounts)
                    }
                }
                is AccountMainViewState.UpdateAccount -> {
                    openAccountFormPage(viewState.accoundId)
                }
                is AccountMainViewState.GetTransactions -> {
                    binding.textIncomeAmount.text =
                        viewState.incomeAmount?.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
                    binding.textExpenseAmount.text =
                        viewState.expenseAmount?.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
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

    private fun setUpEmptyState() {
        bindingEmptyState.apply {
            textTitle.apply {
                text = getString(R.string.account_main_empty_state_title)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            textDescription.apply {
                text = getString(R.string.account_main_empty_state_description)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
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