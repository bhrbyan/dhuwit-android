package id.dhuwit.core.transaction.model

sealed class TransactionGetType {
    data class GetById(val id: Long) : TransactionGetType()
    data class GetByTransactionType(val transactionType: TransactionType) : TransactionGetType()
}
