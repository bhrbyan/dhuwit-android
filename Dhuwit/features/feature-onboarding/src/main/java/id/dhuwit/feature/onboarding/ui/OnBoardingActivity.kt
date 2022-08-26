package id.dhuwit.feature.onboarding.ui

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.account.model.AccountOnBoarding
import id.dhuwit.core.base.state.ViewState
import id.dhuwit.core.setting.user.SettingUser
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.onboarding.R
import id.dhuwit.feature.onboarding.databinding.OnBoardingActivityBinding
import id.dhuwit.feature.onboarding.ui.adapter.OnBoardingAccountAdapter
import id.dhuwit.feature.onboarding.ui.adapter.OnBoardingAccountListener
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : id.dhuwit.core.base.base.BaseActivity(), OnBoardingAccountListener {

    private lateinit var binding: OnBoardingActivityBinding
    private lateinit var adapterAccount: OnBoardingAccountAdapter

    private val viewModel: OnBoardingViewModel by viewModels()

    @Inject
    lateinit var accountRouter: AccountRouter

    @Inject
    lateinit var settingUser: SettingUser

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
                        openAccountMaindPage()
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

        binding.recyclerViewAccount.apply {
            adapter = adapterAccount
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getDefaultAccounts(): List<AccountOnBoarding> {
        return listOf(
            AccountOnBoarding(
                name = getString(R.string.on_boarding_default_account_checking_title),
                description = getString(R.string.on_boarding_default_account_checking_description),
                icon = ContextCompat.getDrawable(this, R.drawable.ic_wallet),
                backgroundColor = ContextCompat.getColor(this, R.color.chantilly)
            ),
            AccountOnBoarding(
                name = getString(R.string.on_boarding_default_account_credit_card_title),
                description = getString(R.string.on_boarding_default_account_credit_card_description),
                icon = ContextCompat.getDrawable(this, R.drawable.ic_credit_card),
                backgroundColor = ContextCompat.getColor(this, R.color.sail)
            ),
            AccountOnBoarding(
                name = getString(R.string.on_boarding_default_account_cash_title),
                description = getString(R.string.on_boarding_default_account_cash_description),
                icon = ContextCompat.getDrawable(this, R.drawable.ic_cash),
                backgroundColor = ContextCompat.getColor(this, R.color.zanah)
            ),
            AccountOnBoarding(
                name = getString(R.string.on_boarding_default_account_other_title),
                description = getString(R.string.on_boarding_default_account_other_description),
                icon = ContextCompat.getDrawable(this, R.drawable.ic_bank),
                backgroundColor = ContextCompat.getColor(this, R.color.buttermilk)
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

    private fun openAccountMaindPage() {
        startActivity(accountRouter.openAccountMainPage(this))
        finish()
    }
}