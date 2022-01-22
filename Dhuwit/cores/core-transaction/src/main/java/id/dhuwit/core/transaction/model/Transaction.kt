package id.dhuwit.core.transaction.model

import id.dhuwit.core.category.model.Category
import id.dhuwit.core.transaction.database.TransactionEntity


data class Transaction(
    var type: TransactionType,
    var amount: Double,
    var note: String?,
    var date: String,
    var createdAt: String,
    var category: Category?,
    var accountId: Long,
    var id: Long = 0,
) {
    fun toEntity(): TransactionEntity {
        return TransactionEntity(
            id = id,
            type = type.toString(),
            amount = amount,
            note = note,
            date = date,
            createdAt = createdAt,
            categoryId = category?.id ?: 0,
            categoryName = category?.name ?: "",
            categoryType = category?.type.toString(),
            accountId = accountId
        )
    }
}