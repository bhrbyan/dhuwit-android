package id.dhuwit.core.currency.model

import com.google.gson.annotations.SerializedName

data class CurrencyData(
    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("symbol_native")
    val symbolNative: String,

    @SerializedName("decimal_digits")
    val decimalDigits: Int,

    @SerializedName("rounding")
    val rounding: Int,

    @SerializedName("code")
    val code: String,

    @SerializedName("name_plural")
    val namePlural: String
) {
    fun toModel(): Currency = Currency(
        symbol = symbol,
        name = name,
        symbolNative = symbolNative,
        decimalDigits = decimalDigits,
        rounding = rounding,
        code = code,
        namePlural = namePlural
    )
}