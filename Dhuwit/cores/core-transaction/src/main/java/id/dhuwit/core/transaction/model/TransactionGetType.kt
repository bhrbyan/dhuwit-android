package id.dhuwit.core.transaction.model

sealed class TransactionGetType {

    object None : TransactionGetType()

    data class ByAccountId(val accountId: Long?) : TransactionGetType()

}
