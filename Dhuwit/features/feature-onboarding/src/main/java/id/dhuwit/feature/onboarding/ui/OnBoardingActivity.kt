package id.dhuwit.feature.onboarding.ui

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.onboarding.R
import id.dhuwit.feature.onboarding.databinding.OnBoardingActivityBinding
import id.dhuwit.feature.onboarding.ui.adapter.OnBoardingAccountAdapter
import id.dhuwit.feature.onboarding.ui.adapter.OnBoardingAccountListener
import id.dhuwit.state.ViewState
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity(), OnBoardingAccountListener {

    private lateinit var binding: OnBoardingActivityBinding
    private lateinit var adapterAccount: OnBoardingAccountAdapter

    private val viewModel: OnBoardingViewModel by viewModels()

    @Inject
    lateinit var accountRouter: AccountRouter

    @Inject
    lateinit var storage: Storage

    override fun init() {
        binding = OnBoardingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setUpAdapterAccount()
    }

    override fun listener() {
        // Do nothing
    }

    override fun observer() {
        with(viewModel) {

            viewState.observe(this@OnBoardingActivity) {
                when (it) {
                    is OnBoardingViewState.SuccessCreateAccount -> {
                        openDashboardPage()
                    }
                    is ViewState.Error -> {
                        showError()
                    }

                }
            }
        }
    }

    private fun setUpAdapterAccount() {
        adapterAccount = OnBoardingAccountAdapter().apply {
            listener = this@OnBoardingActivity
            submitList(getDefaultAccounts())
        }

        binding.recyclerViewAccount?.apply {
            adapter = adapterAccount
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getDefaultAccounts(): List<Account> {
        return listOf(
            Account(
                name = getString(R.string.on_boarding_default_account_checking_title),
                balance = DEFAULT_AMOUNT_ACCOUNT,
                isPrimary = true
            ),
            Account(
                name = getString(R.string.on_boarding_default_account_credit_card_title),
                balance = DEFAULT_AMOUNT_ACCOUNT,
                isPrimary = true
            ),
            Account(
                name = getString(R.string.on_boarding_default_account_cash_title),
                balance = DEFAULT_AMOUNT_ACCOUNT,
                isPrimary = true
            ),
            Account(
                name = getString(R.string.on_boarding_default_account_other_title),
                balance = DEFAULT_AMOUNT_ACCOUNT,
                isPrimary = true
            )
        )
    }

    override fun onSelectAccount(account: Account) {
        viewModel.createAccount(account)
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.general_error_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun openDashboardPage() {
        startActivity(accountRouter.openAccountMainPage(this))
        finish()
    }

    companion object {
        private const val DEFAULT_AMOUNT_ACCOUNT: Double = 0.0
    }
}