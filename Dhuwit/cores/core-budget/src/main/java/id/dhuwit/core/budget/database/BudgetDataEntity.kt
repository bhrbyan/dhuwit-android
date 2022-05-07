package id.dhuwit.core.budget.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dhuwit.core.budget.model.BudgetData
import id.dhuwit.core.budget.model.BudgetDataType
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType

@Entity(tableName = "budget_data_table")
data class BudgetDataEntity(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "budget_id") val budgetId: Long,
    @ColumnInfo(name = "budget_type") var budgetType: String,
    @ColumnInfo(name = "budget_amount") var budgetAmount: Double,
    @ColumnInfo(name = "budget_remaining") var budgetRemainingAmount: Double,
    @ColumnInfo(name = "category_id") var categoryId: Long,
    @ColumnInfo(name = "category_name") var categoryName: String,
    @ColumnInfo(name = "category_type") var categoryType: String,
) {
    fun toModel(): BudgetData {
        return BudgetData(
            budgetId, BudgetDataType.getBudgetDataType(budgetType),
            budgetAmount,
            budgetRemainingAmount,
            Category(categoryName, CategoryType.getCategoryType(categoryType), categoryId)
        )
    }
}
