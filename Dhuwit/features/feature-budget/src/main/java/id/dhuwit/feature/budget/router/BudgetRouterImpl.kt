package id.dhuwit.feature.budget.router

import androidx.fragment.app.Fragment
import id.dhuwit.feature.budget.ui.BudgetFragment

object BudgetRouterImpl : BudgetRouter {
    override fun openBudgetPage(): Fragment {
        return BudgetFragment()
    }
}