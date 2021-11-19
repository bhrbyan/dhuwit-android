package id.dhuwit.core.currency.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.dhuwit.core.currency.R
import id.dhuwit.core.currency.model.Currency
import id.dhuwit.core.currency.model.CurrencyData
import java.io.IOException

object CurrencyUtil {

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
            jsonString = context.resources.openRawResource(R.raw.currency)
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

}