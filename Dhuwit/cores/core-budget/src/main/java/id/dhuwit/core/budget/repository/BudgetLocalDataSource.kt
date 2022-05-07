package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.database.BudgetDao
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetData
import id.dhuwit.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BudgetLocalDataSource @Inject constructor(private val dao: BudgetDao) : BudgetDataSource {

    override suspend fun saveBudget(budget: Budget): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.saveBudget(budget.toEntity())
                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getBudgets(): State<List<Budget>> {
        return withContext(Dispatchers.IO) {
            try {
                val budgets = dao.getBudgets().map {
                    it.toModel()
                }

                State.Success(budgets)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getBudget(budgetId: Long?): State<Budget> {
        return withContext(Dispatchers.IO) {
            try {
                val budget = dao.getBudget(budgetId).toModel()

                State.Success(budget)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getBudgetData(budgetId: Long?, date: String?): State<List<BudgetData>> {
        return withContext(Dispatchers.IO) {
            try {
                val budgetData = dao.getBudgetsData(budgetId).map { it.toModel() }

                State.Success(budgetData)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }
}