package id.dhuwit.feature.account.ui.main

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountMainActivityBinding
import id.dhuwit.feature.account.ui.main.adapter.AccountMainAdapter
import id.dhuwit.state.ViewState
import id.dhuwit.uikit.databinding.EmptyStateBinding
import id.dhuwit.uikit.databinding.ToolbarBinding

@AndroidEntryPoint
class AccountMainActivity : BaseActivity() {

    private lateinit var binding: AccountMainActivityBinding
    private lateinit var bindingToolbar: ToolbarBinding
    private lateinit var bindingEmptyState: EmptyStateBinding
    private lateinit var viewPagerCallback: ViewPager2.OnPageChangeCallback
    private lateinit var viewPagerAdapter: AccountMainAdapter

    private val viewModel: AccountMainViewModel by viewModels()

    override fun init() {
        binding = AccountMainActivityBinding.inflate(layoutInflater)
        bindingToolbar = binding.layoutToolbar
        bindingEmptyState = binding.layoutEmptyState
        setContentView(binding.root)

        setUpToolbar()
        setUpViewPagerAdapter()
        setUpEmptyState()
    }

    override fun listener() {
        viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // get data
            }
        }
        binding.viewPager.registerOnPageChangeCallback(viewPagerCallback)
    }

    override fun observer() {
        viewModel.viewState.observe(this) { viewState ->
            when (viewState) {
                is AccountMainViewState.GetAccounts -> {
                    if (viewState.accounts.isNullOrEmpty()) {
                        showEmptyState()
                    } else {
                        hideEmptyState()
                        setTabLayoutName(viewState.accounts)
                        viewPagerAdapter.updateAccounts(viewState.accounts)
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
        viewPagerAdapter = AccountMainAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
    }

    private fun setTabLayoutName(accounts: List<Account>) {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = accounts[position].name
        }.attach()
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