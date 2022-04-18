package id.dhuwit.feature.main

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.budget.router.BudgetRouter
import id.dhuwit.feature.main.databinding.MainActivityBinding
import id.dhuwit.feature.overview.router.DashboardRouter
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: MainActivityBinding

    @Inject
    lateinit var budgetRouter: BudgetRouter

    @Inject
    lateinit var dashboardRouter: DashboardRouter

    @Inject
    lateinit var accountRouter: AccountRouter

    override fun init() {
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ToDo: Update this to prevent open dashboard page when rotate screen
        openMenu(dashboardRouter.openDashboardPage())
    }

    override fun listener() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_overview -> openMenu(dashboardRouter.openDashboardPage())
                R.id.menu_account -> openMenu(accountRouter.openAccountListPage())
                R.id.menu_budget -> openMenu(budgetRouter.openBudgetPage())
                else -> throw Exception("Menu Item Not Found!")
            }

            true
        }
    }

    override fun observer() {

    }

    private fun openMenu(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, fragment)
            .commit()
    }
}