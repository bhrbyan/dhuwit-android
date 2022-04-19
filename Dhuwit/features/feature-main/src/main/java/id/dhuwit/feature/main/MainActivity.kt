package id.dhuwit.feature.main

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.budget.router.BudgetRouter
import id.dhuwit.feature.main.databinding.MainActivityBinding
import id.dhuwit.feature.overview.router.OverviewRouter
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: MainActivityBinding
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var budgetRouter: BudgetRouter

    @Inject
    lateinit var overviewRouter: OverviewRouter

    @Inject
    lateinit var accountRouter: AccountRouter

    override fun init() {
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun listener() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_overview -> viewModel.openOverviewPage()
                R.id.menu_account -> viewModel.openAccountPage()
                R.id.menu_budget -> viewModel.openBudgetPage()
                else -> throw Exception("Menu Item Not Found!")
            }

            true
        }
    }

    override fun observer() {
        viewModel.viewState.observe(this) {
            when (it) {
                is MainViewState.OpenOverviewPage -> openMenu(overviewRouter.openOverviewPage())
                is MainViewState.OpenAccountPage -> openMenu(accountRouter.openAccountListPage())
                is MainViewState.OpenBudgetPage -> openMenu(budgetRouter.openBudgetPage())
            }
        }
    }

    private fun openMenu(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, fragment)
            .commit()
    }
}