package id.dhuwit.core.currency.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.dhuwit.core.currency.model.Currency
import id.dhuwit.core.currency.model.CurrencyData
import java.io.IOException

object CurrencyUtil {

    private const val CURRENCY_FILE_NAME = "currency.json"

    fun getCurrencies(context: Context): List<Currency> {
        val currencyJsonFileString = getJsonDataFromAsset(context)
        val listCurrencyType = object : TypeToken<List<CurrencyData>>() {}.type
        val currenciesData: List<CurrencyData> =
            Gson().fromJson(currencyJsonFileString, listCurrencyType)

        return currenciesData.map { it.toModel() }
    }

    private fun getJsonDataFromAsset(context: Context): String? {
        val jsonString: String
        try {
            jsonString =
                context.assets.open(CURRENCY_FILE_NAME).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

}