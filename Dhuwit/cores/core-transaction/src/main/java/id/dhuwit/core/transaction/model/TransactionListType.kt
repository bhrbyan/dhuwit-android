package id.dhuwit.core.transaction.model

sealed class TransactionListType {
    object Income : TransactionListType()
    object Expense : TransactionListType()
    object Category : TransactionListType()
    object Account : TransactionListType()

    companion object {
        private const val INCOME: String = "Income"
        private const val EXPENSE: String = "Expense"
        private const val CATEGORY: String = "Category"
        private const val ACCOUNT: String = "Account"

        fun convertToTransactionListType(transactionListType: String?): TransactionListType {
            return when (transactionListType) {
                INCOME -> Income
                EXPENSE -> Expense
                CATEGORY -> Category
                ACCOUNT -> Account
                else -> throw Exception("Unknown Transaction List Type")
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            is Income -> INCOME
            is Expense -> EXPENSE
            is Category -> CATEGORY
            is Account -> ACCOUNT
        }
    }

}
