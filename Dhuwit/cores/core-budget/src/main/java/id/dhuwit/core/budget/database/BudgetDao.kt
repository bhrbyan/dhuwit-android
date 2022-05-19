package id.dhuwit.core.budget.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BudgetDao {

    @Insert
    suspend fun saveBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budget_table")
    suspend fun getBudgets(): List<BudgetEntity>

    @Query("SELECT * FROM budget_table WHERE id = :budgetId")
    suspend fun getBudget(budgetId: Long?): BudgetEntity

    @Query("SELECT * FROM budget_plan_table WHERE budget_id = :budgetId")
    suspend fun getBudgetPlans(budgetId: Long?): List<BudgetPlanEntity>

    @Query("SELECT * FROM budget_plan_table WHERE id = :budgetPlanId")
    suspend fun getBudgetPlan(budgetPlanId: Long?): BudgetPlanEntity

    @Insert
    suspend fun saveBudgetPlan(budgetPlanEntity: BudgetPlanEntity?)

    @Update
    suspend fun updateBudgetPlan(budgetPlanEntity: BudgetPlanEntity?)

    @Query("DELETE FROM budget_plan_table WHERE id = :budgetPlanId")
    suspend fun deleteBudgetPlan(budgetPlanId: Long)

}