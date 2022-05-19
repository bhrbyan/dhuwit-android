package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.state.State

interface BudgetDataSource {

    suspend fun saveBudget(budget: Budget): State<Boolean>

    suspend fun getBudgets(): State<List<Budget>>

    suspend fun getBudget(budgetId: Long?): State<Budget>

    suspend fun getBudgetPlans(budgetId: Long?): State<List<BudgetPlan>>

    suspend fun getBudgetPlan(budgetPlanId: Long?): State<BudgetPlan>

    suspend fun saveBudgetPlan(budgetPlan: BudgetPlan?): State<Boolean>

    suspend fun updateBudgetPlan(budgetPlan: BudgetPlan?): State<Boolean>

    suspend fun deleteBudgetPlan(budgetPlanId: Long): State<Boolean>

}