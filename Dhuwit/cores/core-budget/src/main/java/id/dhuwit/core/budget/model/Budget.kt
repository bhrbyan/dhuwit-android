package id.dhuwit.core.budget.model

import id.dhuwit.core.budget.database.BudgetEntity

data class Budget(
    val id: Long,
    var name: String,
    val periodType: BudgetPeriodType,
    val periodDate: Int
) {
    fun toEntity(): BudgetEntity {
        return BudgetEntity(id, name, periodType.toString(), periodDate)
    }
}
