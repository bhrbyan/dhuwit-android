package id.dhuwit.feature.account.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.account.router.AccountRouter
import id.dhuwit.feature.account.router.AccountRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object AccountRouterModule {
    @Provides
    fun provideAccountRouter(): AccountRouter {
        return AccountRouterImpl
    }
}