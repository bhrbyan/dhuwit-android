package id.dhuwit.core.transaction.model

import id.dhuwit.core.transaction.database.TransactionEntity


data class Transaction(
    var type: TransactionType,
    var amount: Double,
    var categoryId: Long,
    var categoryName: String,
    var note: String?,
    var date: String,
    var createdAt: String,
    var id: Long = 0,
) {
    fun toEntity(): TransactionEntity {
        return TransactionEntity(
            id = id,
            type = type.toString(),
            amount = amount,
            categoryId = categoryId,
            categoryName = categoryName,
            note = note,
            date = date,
            createdAt = createdAt
        )
    }
}