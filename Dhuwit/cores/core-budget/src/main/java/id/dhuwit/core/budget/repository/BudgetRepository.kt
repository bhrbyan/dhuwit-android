package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.state.State
import javax.inject.Inject

class BudgetRepository @Inject constructor(private val local: BudgetDataSource) : BudgetDataSource {

    override suspend fun saveBudget(budget: Budget): State<Boolean> {
        return local.saveBudget(budget)
    }

    override suspend fun getBudgets(): State<List<Budget>> {
        return local.getBudgets()
    }

    override suspend fun getBudget(budgetId: Long?): State<Budget> {
        return local.getBudget(budgetId)
    }

    override suspend fun getBudgetPlans(budgetId: Long?): State<List<BudgetPlan>> {
        return local.getBudgetPlans(budgetId)
    }

    override suspend fun getBudgetPlan(budgetPlanId: Long?): State<BudgetPlan> {
        return local.getBudgetPlan(budgetPlanId)
    }

    override suspend fun saveBudgetPlan(budgetPlan: BudgetPlan?): State<Boolean> {
        return local.saveBudgetPlan(budgetPlan)
    }

    override suspend fun updateBudgetPlan(budgetPlan: BudgetPlan?): State<Boolean> {
        return local.updateBudgetPlan(budgetPlan)
    }

    override suspend fun deleteBudgetPlan(budgetPlanId: Long): State<Boolean> {
        return local.deleteBudgetPlan(budgetPlanId)
    }
}