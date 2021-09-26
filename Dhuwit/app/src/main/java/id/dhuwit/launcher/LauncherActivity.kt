package id.dhuwit.launcher

import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currencies = CurrencyUtil.getCurrencies(this)
        val categories = CategoryUtil.getCategories(this)

        viewModel.validationUserStatus(currencies, categories)

        observer()
    }

    override fun observer() {
        with(viewModel) {
            storeData.observe(this@LauncherActivity) { success ->
                if (success) {
                    openOnBoardingPage()
                } else {
                    Log.d("LauncherActivity", "Error store data")
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