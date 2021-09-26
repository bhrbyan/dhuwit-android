package id.dhuwit.core.currency.repository

import id.dhuwit.core.currency.model.Currency
import id.dhuwit.state.State
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val local: CurrencyDataSource) :
    CurrencyDataSource {

    override suspend fun storeCurrencies(currencies: List<Currency>): State<Boolean> {
        return local.storeCurrencies(currencies)
    }

    override suspend fun getCurrency(): State<Currency> {
        return local.getCurrency()
    }
}