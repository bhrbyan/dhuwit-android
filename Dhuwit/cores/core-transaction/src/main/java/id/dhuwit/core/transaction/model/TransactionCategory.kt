package id.dhuwit.core.transaction.model

data class TransactionCategory(
    val categoryId: Long,
    val categoryName: String,
    val totalAmountTransaction: Double,
    val totalTransaction: Int
)
