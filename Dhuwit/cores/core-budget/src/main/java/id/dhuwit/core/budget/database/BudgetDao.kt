package id.dhuwit.core.budget.database

import androidx.room.*

@Dao
interface BudgetDao {

    @Insert
    suspend fun saveBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budget_table")
    suspend fun getBudgets(): List<BudgetEntity>

    @Query("SELECT * FROM budget_table WHERE id = :budgetId")
    suspend fun getBudget(budgetId: Long?): BudgetEntity

    @Query("SELECT * FROM budget_plan_table WHERE budget_id = :budgetId AND budget_type = :budgetPlanType")
    suspend fun getBudgetPlans(budgetId: Long?, budgetPlanType: String): List<BudgetPlanEntity>

    @Query("SELECT * FROM budget_plan_table WHERE id = :budgetPlanId")
    suspend fun getBudgetPlan(budgetPlanId: Long?): BudgetPlanEntity

    @Insert
    suspend fun saveBudgetPlan(budgetPlanEntity: BudgetPlanEntity?)

    @Update
    suspend fun updateBudgetPlan(budgetPlanEntity: BudgetPlanEntity?)

    @Delete
    suspend fun deleteBudgetPlan(budgetPlanEntity: BudgetPlanEntity?)

}