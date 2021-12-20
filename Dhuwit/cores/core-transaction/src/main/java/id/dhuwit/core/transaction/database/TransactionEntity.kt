package id.dhuwit.core.transaction.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionType

@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "amount") var amount: Double,
    @ColumnInfo(name = "note") var note: String?,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "created_at") var createdAt: String,
    @ColumnInfo(name = "category_id") var categoryId: Long,
    @ColumnInfo(name = "category_name") var categoryName: String,
    @ColumnInfo(name = "category_type") var categoryType: String,
    @ColumnInfo(name = "account_id") var accountId: Long
) {
    fun toModel(): Transaction {
        return Transaction(
            type = TransactionType.getTransactionType(type),
            amount = amount,
            note = note,
            date = date,
            createdAt = createdAt,
            category = Category(
                categoryName,
                CategoryType.getCategoryType(categoryType),
                categoryId
            ),
            accountId = accountId,
            id = id
        )
    }
}