package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.state.State
import javax.inject.Inject

class BudgetRepository @Inject constructor(private val local: BudgetDataSource) : BudgetDataSource {

    override suspend fun createBudget(budget: Budget): State<Boolean> {
        return local.createBudget(budget)
    }

    override suspend fun getBudgets(): State<List<Budget>> {
        return local.getBudgets()
    }
}