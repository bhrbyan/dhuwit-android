package id.dhuwit.core.budget.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dhuwit.core.budget.model.Budget
import id.dhuwit.core.budget.model.BudgetCategory
import id.dhuwit.core.budget.model.BudgetSetting

@Entity(tableName = "budget_table")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "name") var name: String
) {
    fun toModel(
        setting: BudgetSetting,
        incomes: List<BudgetCategory>,
        expenses: List<BudgetCategory>
    ): Budget {
        return Budget(id, name, setting, incomes, expenses)
    }
}