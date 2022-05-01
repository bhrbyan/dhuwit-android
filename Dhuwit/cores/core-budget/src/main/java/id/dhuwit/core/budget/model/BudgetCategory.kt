package id.dhuwit.core.budget.model

import id.dhuwit.core.category.model.Category

data class BudgetCategory(
    val budgetId: Long,
    val budgetCategoryType: BudgetCategoryType,
    val category: Category,
    val amount: Double
)
