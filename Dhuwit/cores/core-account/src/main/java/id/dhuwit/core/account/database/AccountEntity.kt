package id.dhuwit.core.account.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dhuwit.core.account.model.Account

@Entity(tableName = "account_table")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "balance") var balance: Double,
    @ColumnInfo(name = "is_primary") var isPrimary: Boolean
) {
    fun toModel(): Account {
        return Account(name, balance, isPrimary, id)
    }
}
