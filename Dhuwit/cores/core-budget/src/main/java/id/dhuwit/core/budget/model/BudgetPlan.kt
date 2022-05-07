package id.dhuwit.core.budget.model

import id.dhuwit.core.category.model.Category

data class BudgetPlan(
    val budgetId: Long,
    var budgetPlanType: BudgetPlanType,
    var budgetAmount: Double,
    var budgetRemainingAmount: Double,
    val category: Category
)
