package id.dhuwit.core.base.helper

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    const val PATTERN_DATE_TRANSACTION: String = "EEEE, dd MMMM yyyy"
    const val PATTERN_DATE_DATABASE: String = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    const val PATTERN_DATE_PERIOD: String = "MMMM yyyy"

    const val PATTERN_TIME_DATABASE: String = "HH:mm:ss"
    const val PATTERN_TIME: String = "HH:mm"

    fun getCurrentDate(pattern: String): String {
        return Calendar.getInstance()
            .patternFormat(pattern)
    }

    fun getDate(pattern: String, count: Int): String {
        return Calendar.getInstance()
            .apply { add(Calendar.DAY_OF_YEAR, count) }
            .patternFormat(pattern)
    }

    fun getPeriodDate(count: Int, pattern: String): String {
        return Calendar.getInstance()
            .apply { add(Calendar.MONTH, count) }
            .patternFormat(pattern)
    }

    fun isTransactionDateWithinRangePeriodDate(
        transactionDate: String,
        periodDate: String
    ): Boolean {
        return periodDate == transactionDate
    }

    /* Extension */

    private fun Calendar.patternFormat(pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(this.time)
    }

    fun String.convertToMillis(pattern: String): Long? {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date: Date? = simpleDateFormat.parse(this)
        return date?.time
    }

    fun Long.convertToDate(pattern: String): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val calendar = Calendar.getInstance().apply { timeInMillis = this@convertToDate }
        return simpleDateFormat.format(calendar.time)
    }

    fun String.convertPattern(fromPattern: String, toPattern: String): String {
        val simpleDateFormat = SimpleDateFormat(fromPattern, Locale.getDefault())
        val formattedDate = simpleDateFormat.parse(this) ?: String()
        simpleDateFormat.applyPattern(toPattern)
        return simpleDateFormat.format(formattedDate)
    }
}
