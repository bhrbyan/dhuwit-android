package id.dhuwit.core.budget.repository

import id.dhuwit.core.budget.database.BudgetDao
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPlanType
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
                val budgets = dao.getBudgets().map {
                    val budgetSetting = dao.getBudgetSetting(it.id).toModel()
                    val budgetIncomes =
                        dao.getBudgetPlans(it.id, BudgetPlanType.Income.toString())
                            .map { budgetPlanEntity ->
                                budgetPlanEntity.toModel()
                            }
                    val budgetExpenses =
                        dao.getBudgetPlans(it.id, BudgetPlanType.Expense.toString())
                            .map { budgetPlanEntity ->
                                budgetPlanEntity.toModel()
                            }

                    it.toModel(budgetSetting, budgetIncomes, budgetExpenses)
                }

                State.Success(budgets)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getBudget(budgetId: Long): State<Budget> {
        return withContext(Dispatchers.IO) {
            try {
                val budgetSetting = dao.getBudgetSetting(budgetId).toModel()
                val budgetIncomes =
                    dao.getBudgetPlans(budgetId, BudgetPlanType.Income.toString())
                        .map { budgetPlanEntity ->
                            budgetPlanEntity.toModel()
                        }
                val budgetExpenses =
                    dao.getBudgetPlans(budgetId, BudgetPlanType.Expense.toString())
                        .map { budgetPlanEntity ->
                            budgetPlanEntity.toModel()
                        }

                val budget =
                    dao.getBudget(budgetId).toModel(budgetSetting, budgetIncomes, budgetExpenses)

                State.Success(budget)
            } catch (e: Exception) {
                State.Error(e.localizedMessage)
            }
        }
    }
}