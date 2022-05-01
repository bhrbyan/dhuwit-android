package id.dhuwit.core.budget.model

sealed class BudgetCategoryType {

    object Income : BudgetCategoryType()
    object Expense : BudgetCategoryType()

    companion object {
        private const val INCOME: String = "Income"
        private const val EXPENSE: String = "Expense"

        fun getBudgetCategoryType(categoryType: String?): BudgetCategoryType {
            return when (categoryType) {
                INCOME -> Income
                EXPENSE -> Expense
                else -> throw Exception("Unknown Budget Category Type")
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            is Income -> INCOME
            is Expense -> EXPENSE
            else -> throw Exception("Unknown Budget Category Type")
        }
    }

}
