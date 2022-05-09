package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.database.BudgetDao
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
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

    override suspend fun getBudgetPlans(
        budgetId: Long?,
        budgetPlanType: BudgetPlanType
    ): State<List<BudgetPlan>> {
        return withContext(Dispatchers.IO) {
            try {
                val budgetPlan =
                    dao.getBudgetPlans(budgetId, budgetPlanType.toString()).map { it.toModel() }

                State.Success(budgetPlan)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getBudgetPlan(budgetPlanid: Long?): State<BudgetPlan> {
        return withContext(Dispatchers.IO) {
            try {
                val budgetPlan = dao.getBudgetPlan(budgetPlanid).toModel()

                State.Success(budgetPlan)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun saveBudgetPlan(budgetPlan: BudgetPlan?): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.saveBudgetPlan(budgetPlan?.toEntity())
                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun updateBudgetPlan(budgetPlan: BudgetPlan?): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.updateBudgetPlan(budgetPlan?.toEntity())
                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun deleteBudgetPlan(budgetPlan: BudgetPlan?): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                dao.deleteBudgetPlan(budgetPlan?.toEntity())
                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }
}