package id.dhuwit.core.currency.repository

import id.dhuwit.core.currency.model.Currency
import id.dhuwit.state.State

interface CurrencyDataSource {
    suspend fun storeCurrencies(currencies: List<Currency>): State<Boolean>
    suspend fun getCurrency(): State<Currency>
}