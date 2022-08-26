package id.dhuwit.core.account.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.dhuwit.core.account.database.AccountDao
import id.dhuwit.core.account.database.AccountDatabase
import id.dhuwit.core.account.repository.AccountDataSource
import id.dhuwit.core.account.repository.AccountLocalDataSource
import id.dhuwit.core.account.repository.AccountRepository
import id.dhuwit.core.base.di.LocalSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountRepositoryModule {
    @Provides
    @Singleton
    fun provideAccountRepository(@LocalSource local: AccountDataSource): AccountDataSource {
        return AccountRepository(local)
    }

    @Provides
    @LocalSource
    fun provideAccountLocalDataSource(dao: AccountDao): AccountDataSource {
        return AccountLocalDataSource(dao)
    }

    @Provides
    fun provideAccountDao(@ApplicationContext context: Context): AccountDao {
        return AccountDatabase.getInstance(context).accountDao()
    }

}