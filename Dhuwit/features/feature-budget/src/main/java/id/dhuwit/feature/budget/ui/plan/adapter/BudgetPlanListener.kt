package id.dhuwit.feature.budget.ui.plan.adapter

interface BudgetPlanListener {
    fun onClickItem(categoryId: Long, amount: Double?)
}