package id.dhuwit.core.account.model

import id.dhuwit.core.account.database.AccountEntity

data class Account(
    val name: String,
    val balance: Double,
    val id: Long = 0
) {
    fun toEntity(): AccountEntity {
        return AccountEntity(id, name, balance)
    }
}
