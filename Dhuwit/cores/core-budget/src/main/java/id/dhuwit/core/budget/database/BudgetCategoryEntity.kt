package id.dhuwit.core.budget.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dhuwit.core.budget.model.BudgetCategory
import id.dhuwit.core.budget.model.BudgetCategoryType
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType

@Entity(tableName = "budget_category_table")
data class BudgetCategoryEntity(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "budget_id") val budgetId: Long,
    @ColumnInfo(name = "budget_category_type") var budgetCategoryType: String,
    @ColumnInfo(name = "category_id") var categoryId: Long,
    @ColumnInfo(name = "name") var categoryName: String,
    @ColumnInfo(name = "type") var categoryType: String,
    @ColumnInfo(name = "amount") var budgetAmount: Double
) {
    fun toModel(): BudgetCategory {
        return BudgetCategory(
            budgetId,
            BudgetCategoryType.getBudgetCategoryType(budgetCategoryType),
            Category(categoryName, CategoryType.getCategoryType(categoryType), categoryId),
            budgetAmount
        )
    }
}
