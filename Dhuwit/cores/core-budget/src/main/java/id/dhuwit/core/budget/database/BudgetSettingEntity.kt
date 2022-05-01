package id.dhuwit.core.budget.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dhuwit.core.budget.model.BudgetPeriodType
import id.dhuwit.core.budget.model.BudgetSetting

@Entity(tableName = "budget_setting_table")
data class BudgetSettingEntity(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "budget_id") val budgetId: Long,
    @ColumnInfo(name = "period_type") val periodType: String,
    @ColumnInfo(name = "period_date") val periodDate: Int
) {
    fun toModel(): BudgetSetting {
        return BudgetSetting(
            budgetId, BudgetPeriodType.getBudgetPeriodType(periodType), periodDate
        )
    }
}
