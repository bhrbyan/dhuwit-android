package id.dhuwit.core.budget.model

sealed class BudgetPlanType {

    object Income : BudgetPlanType()
    object Expense : BudgetPlanType()

    companion object {
        private const val INCOME: String = "Income"
        private const val EXPENSE: String = "Expense"

        fun getBudgetPlanType(planType: String?): BudgetPlanType {
            return when (planType) {
                INCOME -> Income
                EXPENSE -> Expense
                else -> throw Exception("Unknown Budget Plan Type")
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            is Income -> INCOME
            is Expense -> EXPENSE
            else -> throw Exception("Unknown Budget Plan Type")
        }
    }

}
