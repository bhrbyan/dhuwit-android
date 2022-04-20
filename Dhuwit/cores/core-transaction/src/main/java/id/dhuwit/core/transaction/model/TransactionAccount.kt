package id.dhuwit.core.transaction.model

data class TransactionAccount(
    val accountId: Long,
    val accountName: String?,
    val totalAmountTransaction: Double,
    val totalTransaction: Int
)
