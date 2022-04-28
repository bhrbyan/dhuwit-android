package id.dhuwit.core.budget.model

import id.dhuwit.core.budget.database.BudgetEntity

data class Budget(
    val id: Long,
    val name: String
) {
    fun toEntity(): BudgetEntity {
        return BudgetEntity(
            id, name
        )
    }
}
