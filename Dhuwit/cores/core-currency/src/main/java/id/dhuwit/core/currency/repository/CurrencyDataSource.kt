package id.dhuwit.core.currency.repository

import id.dhuwit.core.base.state.State
import id.dhuwit.core.currency.model.Currency

interface CurrencyDataSource {
    suspend fun storeCurrencies(currencies: List<Currency>): State<Boolean>
    suspend fun getCurrency(): State<Currency>
}