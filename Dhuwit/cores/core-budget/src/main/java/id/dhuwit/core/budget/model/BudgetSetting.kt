package id.dhuwit.core.budget.model

data class BudgetSetting(
    val budgetId: Long? = null,
    val periodType: BudgetPeriodType = BudgetPeriodType.Monthly,
    val periodDate: Int = 1
)
