package id.dhuwit.core.currency.repository

import id.dhuwit.core.currency.database.CurrencyDao
import id.dhuwit.core.currency.model.Currency
import id.dhuwit.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyLocalDataSource @Inject constructor(private val dao: CurrencyDao) :
    CurrencyDataSource {
    override suspend fun storeCurrencies(currencies: List<Currency>): State<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                currencies.forEach {
                    dao.storeCurrency(it.toEntity())
                }
                State.Success(true)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }

    override suspend fun getCurrency(): State<Currency> {
        return withContext(Dispatchers.IO) {
            try {
                val currency = dao.getCurrency().toModel()
                State.Success(currency)
            } catch (e: Exception) {
                State.Error(e.localizedMessage ?: "")
            }
        }
    }
}