package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.state.State

interface BudgetDataSource {

    suspend fun createBudget(budget: Budget): State<Boolean>

    suspend fun getBudgets(): State<List<Budget>>

    suspend fun getBudget(budgetId: Long): State<Budget>

    var budgetPlanIncomesTemp: List<BudgetPlan>?

    var budgetPlanExpensesTemp: List<BudgetPlan>?

}