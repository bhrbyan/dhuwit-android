package id.dhuwit.core.category.model

sealed class CategoryType {
    object Income : CategoryType()
    object Expense : CategoryType()

    companion object {
        private const val INCOME: String = "Income"
        private const val EXPENSE: String = "Expense"

        fun getCategoryType(type: String?): CategoryType {
            return when (type) {
                INCOME -> Income
                EXPENSE -> Expense
                else -> throw Exception("Category type not found")
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            Income -> INCOME
            Expense -> EXPENSE
        }
    }

}
