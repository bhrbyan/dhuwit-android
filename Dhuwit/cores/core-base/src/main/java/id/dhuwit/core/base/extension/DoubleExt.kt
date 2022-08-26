package id.dhuwit.core.base.extension

import java.text.DecimalFormat
import java.text.NumberFormat

private const val PATTERN_PRICE: String = "#,###"

fun Double.convertPriceWithCurrencyFormat(currencySymbol: String?): String {
    val formatter: NumberFormat = DecimalFormat(PATTERN_PRICE)
    return setFormat(this, formatter, currencySymbol)
}

private fun setFormat(amount: Double, formatter: NumberFormat, currencySymbol: String?): String {
    return "$currencySymbol${formatter.format(amount)}"
}

fun Double.convertDoubleToString(): String {
    return String.format("%.0f", this)
}