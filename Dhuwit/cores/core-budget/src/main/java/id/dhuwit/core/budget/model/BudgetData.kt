package id.dhuwit.core.budget.model

import id.dhuwit.core.category.model.Category

data class BudgetData(
    val budgetId: Long,
    var budgetDataType: BudgetDataType,
    var budgetAmount: Double,
    var budgetRemainingAmount: Double,
    val category: Category
)
