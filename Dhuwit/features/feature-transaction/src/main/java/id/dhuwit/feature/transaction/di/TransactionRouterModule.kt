package id.dhuwit.feature.transaction.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.transaction.router.TransactionRouter
import id.dhuwit.feature.transaction.router.TransactionRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object TransactionRouterModule {
    @Provides
    fun provideTransactionRouter(): TransactionRouter {
        return TransactionRouterImpl
    }
}