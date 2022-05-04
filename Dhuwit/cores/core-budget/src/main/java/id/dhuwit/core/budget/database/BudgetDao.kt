package id.dhuwit.core.budget.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BudgetDao {

    @Insert
    suspend fun createBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budget_table")
    suspend fun getBudgets(): List<BudgetEntity>

    @Query("SELECT * FROM budget_table WHERE id = :budgetId")
    suspend fun getBudget(budgetId: Long): BudgetEntity

    @Query("SELECT * FROM budget_setting_table WHERE budget_id = :budgetId")
    suspend fun getBudgetSetting(budgetId: Long?): BudgetSettingEntity

    @Query("SELECT * FROM budget_plan_table WHERE budget_id = :budgetId AND budget_plan_type = :budgetPlanType")
    suspend fun getBudgetPlans(
        budgetId: Long?,
        budgetPlanType: String
    ): List<BudgetPlanEntity>

}