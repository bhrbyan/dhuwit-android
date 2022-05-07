package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetData
import id.dhuwit.state.State

interface BudgetDataSource {

    suspend fun saveBudget(budget: Budget): State<Boolean>

    suspend fun getBudgets(): State<List<Budget>>

    suspend fun getBudget(budgetId: Long?): State<Budget>

    suspend fun getBudgetData(budgetId: Long?, date: String?): State<List<BudgetData>>

}