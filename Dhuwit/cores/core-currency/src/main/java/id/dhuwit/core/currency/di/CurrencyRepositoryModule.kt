package id.dhuwit.core.currency.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.dhuwit.core.currency.database.CurrencyDao
import id.dhuwit.core.currency.database.CurrencyDatabase
import id.dhuwit.core.currency.repository.CurrencyDataSource
import id.dhuwit.core.currency.repository.CurrencyLocalDataSource
import id.dhuwit.core.currency.repository.CurrencyRepository
import id.dhuwit.core.di.LocalSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CurrencyRepositoryModule {

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        @LocalSource local: CurrencyDataSource
    ): CurrencyDataSource {
        return CurrencyRepository(local)
    }

    @Provides
    @LocalSource
    fun provideCurrencyLocalDataSource(dao: CurrencyDao): CurrencyDataSource {
        return CurrencyLocalDataSource(dao)
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(@ApplicationContext context: Context): CurrencyDao {
        return CurrencyDatabase.getInstance(context).currencyDao()
    }

}