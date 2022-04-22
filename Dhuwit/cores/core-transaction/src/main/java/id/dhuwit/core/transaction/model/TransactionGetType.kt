package id.dhuwit.core.transaction.model

sealed class TransactionGetType {
    object GetAll : TransactionGetType()
    data class GetByTransactionType(val transactionType: TransactionType) : TransactionGetType()
    data class GetByCategoryId(val categoryId: Long) : TransactionGetType()
}
