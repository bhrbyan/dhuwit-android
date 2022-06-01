package id.dhuwit.core.transaction.model

sealed class TransactionGetBy {

    object None : TransactionGetBy()

    data class ByAccountId(val accountId: Long?) : TransactionGetBy()

}
