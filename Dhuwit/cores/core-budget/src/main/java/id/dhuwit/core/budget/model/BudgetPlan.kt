package id.dhuwit.core.budget.model

import id.dhuwit.core.budget.database.BudgetPlanEntity
import id.dhuwit.core.category.model.Category

data class BudgetPlan(
    val budgetId: Long?,
    val budgetPlanType: BudgetPlanType,
    val category: Category,
    val amount: Double?
) {
    fun toBudgetPlanEntity(budgetId: Long?): BudgetPlanEntity {
        return BudgetPlanEntity(
            id = 0,
            budgetId = budgetId ?: 0,
            budgetPlanType = budgetPlanType.toString(),
            categoryId = category.id,
            categoryName = category.name,
            categoryType = category.type.toString(),
            budgetAmount = amount ?: 0.0
        )
    }
}
