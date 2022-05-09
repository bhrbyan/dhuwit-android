package id.dhuwit.core.budget.model

import id.dhuwit.core.budget.database.BudgetPlanEntity
import id.dhuwit.core.category.model.Category

data class BudgetPlan(
    val budgetId: Long,
    var budgetPlanType: BudgetPlanType,
    var budgetAmount: Double,
    val category: Category,
    val id: Long = 0
) {
    fun toEntity(): BudgetPlanEntity {
        return BudgetPlanEntity(
            id,
            budgetId,
            budgetPlanType.toString(),
            budgetAmount,
            category.id,
            category.name,
            category.type.toString()
        )
    }
}
