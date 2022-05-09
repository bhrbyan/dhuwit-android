package id.dhuwit.core.budget.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.budget.model.BudgetPlanType
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType

@Entity(tableName = "budget_plan_table")
data class BudgetPlanEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "budget_id") val budgetId: Long,
    @ColumnInfo(name = "budget_type") var budgetType: String,
    @ColumnInfo(name = "budget_amount") var budgetAmount: Double,
    @ColumnInfo(name = "category_id") var categoryId: Long,
    @ColumnInfo(name = "category_name") var categoryName: String,
    @ColumnInfo(name = "category_type") var categoryType: String,
) {
    fun toModel(): BudgetPlan {
        return BudgetPlan(
            budgetId,
            BudgetPlanType.getBudgetPlanType(budgetType),
            budgetAmount,
            Category(
                categoryName,
                CategoryType.getCategoryType(categoryType),
                categoryId
            )
        )
    }
}
