package id.dhuwit.core.budget.model

data class BudgetPlanItem(
    val categoryId: Long,
    val categoryName: String,
    var amount: Double?
)
