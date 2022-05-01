package id.dhuwit.core.budget.model

import id.dhuwit.core.budget.database.BudgetEntity

data class Budget(
    val id: Long? = null,
    val name: String = "",
    val setting: BudgetSetting?,
    val incomes: List<BudgetCategory>,
    val expenses: List<BudgetCategory>
) {
    fun toEntity(): BudgetEntity {
        return BudgetEntity(id, name)
    }
}
