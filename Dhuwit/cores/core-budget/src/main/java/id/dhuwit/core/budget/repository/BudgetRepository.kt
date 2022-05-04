package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.state.State
import javax.inject.Inject

class BudgetRepository @Inject constructor(private val local: BudgetDataSource) : BudgetDataSource {

    override suspend fun createBudget(budget: Budget): State<Boolean> {
        return local.createBudget(budget)
    }

    override suspend fun getBudgets(): State<List<Budget>> {
        return local.getBudgets()
    }

    override suspend fun getBudget(budgetId: Long): State<Budget> {
        return local.getBudget(budgetId)
    }

    override var budgetPlanIncomesTemp: List<BudgetPlan>?
        get() = local.budgetPlanIncomesTemp
        set(value) {
            local.budgetPlanIncomesTemp = value
        }

    override var budgetPlanExpensesTemp: List<BudgetPlan>?
        get() = local.budgetPlanExpensesTemp
        set(value) {
            local.budgetPlanExpensesTemp = value
        }
}