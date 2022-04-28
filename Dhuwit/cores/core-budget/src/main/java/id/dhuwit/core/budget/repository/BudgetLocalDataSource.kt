package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.database.BudgetDao
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BudgetLocalDataSource @Inject constructor(private val dao: BudgetDao) : BudgetDataSource {

    override suspend fun createBudget(budget: Budget): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.createBudget(budget.toEntity())

                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getBudgets(): State<List<Budget>> {
        return withContext(Dispatchers.IO) {
            try {
                val budgets = dao.getBudgets().map { it.toModel() }

                State.Success(budgets)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }
}