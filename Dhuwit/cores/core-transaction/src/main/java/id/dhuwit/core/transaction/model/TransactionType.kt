package id.dhuwit.core.transaction.model

sealed class TransactionType {
    object Income : TransactionType()
    object Expense : TransactionType()

    companion object {

        private const val INCOME: String = "Income"
        private const val EXPENSE: String = "Expense"

        fun getTransactionType(type: String): TransactionType {
            return when (type) {
                INCOME -> Income
                EXPENSE -> Expense
                else -> throw IllegalArgumentException("Unknown transaction type!!!")
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            is Income -> INCOME
            is Expense -> EXPENSE
        }
    }

}