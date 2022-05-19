package id.dhuwit.launcher

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.category.util.CategoryUtil
import id.dhuwit.core.currency.util.CurrencyUtil
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.onboarding.router.OnBoardingRouter
import id.dhuwit.storage.Storage
import javax.inject.Inject

@AndroidEntryPoint
class LauncherActivity : BaseActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    @Inject
    lateinit var onBoardingRouter: OnBoardingRouter

    @Inject
    lateinit var accountRouter: AccountRouter

    @Inject
    lateinit var storage: Storage

    override fun init() {
        validateUserStatus()
    }

    override fun listener() {
        // Do nothing
    }

    override fun observer() {
        with(viewModel) {
            storeData.observe(this@LauncherActivity) { success ->
                if (success) {
                    openOnBoardingPage()
                } else {
                    finishAffinity()
                }
            }
        }
    }

    private fun validateUserStatus() {
        if (storage.isFirstTimeUser()) {
            val currencies = CurrencyUtil.getCurrencies(this)
            val categories = CategoryUtil.getCategories(this)

            viewModel.storeDefaultData(currencies, categories)
        } else {
            openDashboardPage()
        }
    }

    private fun openOnBoardingPage() {
        startActivity(onBoardingRouter.openOnBoardingPage(this))
        finish()
    }

    private fun openDashboardPage() {
        startActivity(accountRouter.openAccountMainPage(this))
        finish()
    }
}