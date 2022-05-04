package id.dhuwit.core.budget.model

import id.dhuwit.core.category.model.Category

data class BudgetPlan(
    val budgetId: Long,
    val budgetPlanType: BudgetPlanType,
    val category: Category,
    val amount: Double
)
