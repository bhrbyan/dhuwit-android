package id.dhuwit.core.transaction.model

sealed class GetTransactionType {

    object None : GetTransactionType()

    data class ByAccountId(val accountId: Long?) : GetTransactionType()

}
