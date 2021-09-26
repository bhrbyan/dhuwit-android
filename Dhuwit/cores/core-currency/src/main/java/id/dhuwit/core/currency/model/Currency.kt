package id.dhuwit.core.currency.model

import id.dhuwit.core.currency.database.CurrencyEntity

data class Currency(
    val symbol: String,
    val name: String,
    val symbolNative: String,
    val decimalDigits: Int,
    val rounding: Int,
    val code: String,
    val namePlural: String,
) {
    fun toEntity(): CurrencyEntity {
        return CurrencyEntity(
            code = code,
            symbol = symbol,
            name = name,
            symbolNative = symbolNative,
            decimalDigits = decimalDigits,
            rounding = rounding,
            namePlural = namePlural
        )
    }
}
