package id.dhuwit.core.budget.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BudgetDao {

    @Insert
    suspend fun saveBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budget_table")
    suspend fun getBudgets(): List<BudgetEntity>

    @Query("SELECT * FROM budget_table WHERE id = :budgetId")
    suspend fun getBudget(budgetId: Long?): BudgetEntity

    @Query("SELECT * FROM budget_data_table WHERE id = :budgetId")
    suspend fun getBudgetsData(budgetId: Long?): List<BudgetDataEntity>

}