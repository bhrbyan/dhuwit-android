package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.state.State

interface BudgetDataSource {

    suspend fun createBudget(budget: Budget): State<Boolean>

    suspend fun getBudgets(): State<List<Budget>>

}