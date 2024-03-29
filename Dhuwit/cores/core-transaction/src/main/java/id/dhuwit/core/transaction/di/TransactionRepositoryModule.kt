package id.dhuwit.core.transaction.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.dhuwit.core.account.database.AccountDao
import id.dhuwit.core.base.di.LocalSource
import id.dhuwit.core.transaction.database.TransactionDao
import id.dhuwit.core.transaction.database.TransactionDatabase
import id.dhuwit.core.transaction.repository.TransactionDataSource
import id.dhuwit.core.transaction.repository.TransactionLocalDataSource
import id.dhuwit.core.transaction.repository.TransactionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionRepositoryModule {

    @Provides
    @Singleton
    fun provideTransactionRepository(@LocalSource local: TransactionDataSource): TransactionDataSource {
        return TransactionRepository(local)
    }

    @Provides
    @LocalSource
    fun provideLocalDataSource(
        transactionDao: TransactionDao,
        accountDao: AccountDao
    ): TransactionDataSource {
        return TransactionLocalDataSource(transactionDao, accountDao)
    }

    @Provides
    fun provideTransactionDao(@ApplicationContext context: Context): TransactionDao {
        return TransactionDatabase.getInstance(context).transactionDao()
    }

}