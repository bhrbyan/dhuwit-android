package id.dhuwit.core.currency.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dhuwit.core.currency.model.Currency

@Entity(tableName = "currency_table")
data class CurrencyEntity(
    @PrimaryKey @ColumnInfo(name = "code") var code: String,
    @ColumnInfo(name = "symbol") var symbol: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "symbol_native") var symbolNative: String,
    @ColumnInfo(name = "decimal_digits") var decimalDigits: Int,
    @ColumnInfo(name = "rounding") var rounding: Int,
    @ColumnInfo(name = "name_plural") var namePlural: String
) {
    fun toModel(): Currency {
        return Currency(symbol, name, symbolNative, decimalDigits, rounding, code, namePlural)
    }
}
