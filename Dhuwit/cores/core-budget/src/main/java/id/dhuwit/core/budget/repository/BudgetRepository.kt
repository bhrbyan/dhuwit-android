package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetData
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

    override suspend fun getBudgetData(budgetId: Long?, date: String?): State<List<BudgetData>> {
        return local.getBudgetData(budgetId, date)
    }
}