package id.dhuwit.core.budget.model

sealed class BudgetDataType {

    object Income : BudgetDataType()
    object Expense : BudgetDataType()

    companion object {
        private const val INCOME: String = "Income"
        private const val EXPENSE: String = "Expense"

        fun getBudgetDataType(planType: String?): BudgetDataType {
            return when (planType) {
                INCOME -> Income
                EXPENSE -> Expense
                else -> throw Exception("Unknown Budget Data Type")
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            is Income -> INCOME
            is Expense -> EXPENSE
            else -> throw Exception("Unknown Budget Data Type")
        }
    }

}
