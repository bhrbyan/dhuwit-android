package id.dhuwit.core.transaction.model

sealed class TransactionDeleteBy {

    data class ByTransactionId(val transactionId: Long) : TransactionDeleteBy()

    data class ByAccountId(val accountId: Long?) : TransactionDeleteBy()

}
