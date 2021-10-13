package id.dhuwit.launcher

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.category.util.CategoryUtil
import id.dhuwit.core.currency.util.CurrencyUtil
import id.dhuwit.feature.dashboard.router.DashboardRouter
import id.dhuwit.feature.onboarding.router.OnBoardingRouter
import javax.inject.Inject

@AndroidEntryPoint
class LauncherActivity : BaseActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    @Inject
    lateinit var onBoardingRouter: OnBoardingRouter

    @Inject
    lateinit var dashboardRouter: DashboardRouter

    override fun init() {
        val currencies = CurrencyUtil.getCurrencies(this)
        val categories = CategoryUtil.getCategories(this)

        viewModel.validationUserStatus(currencies, categories)
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

            openDashboard.observe(this@LauncherActivity) { isOpenDashboard ->
                if (isOpenDashboard) {
                    openDashboardPage()
                }
            }
        }

    }

    private fun openOnBoardingPage() {
        startActivity(onBoardingRouter.openOnBoardingPage(this))
        finish()
    }

    private fun openDashboardPage() {
        startActivity(dashboardRouter.openDashboardPage(this))
        finish()
    }
}