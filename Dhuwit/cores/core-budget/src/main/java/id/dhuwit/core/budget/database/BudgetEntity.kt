package id.dhuwit.core.budget.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetPeriodType

@Entity(tableName = "budget_table")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "period_type") val periodType: String,
    @ColumnInfo(name = "period_date") val periodDate: Int
) {
    fun toModel(): Budget {
        return Budget(id, name, BudgetPeriodType.getBudgetPeriodType(periodType), periodDate)
    }
}