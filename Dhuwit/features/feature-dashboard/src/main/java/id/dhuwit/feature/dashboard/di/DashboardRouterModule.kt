package id.dhuwit.feature.dashboard.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.dashboard.router.DashboardRouter
import id.dhuwit.feature.dashboard.router.DashboardRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object DashboardRouterModule {

    @Provides
    fun provideDashboardRouter(): DashboardRouter {
        return DashboardRouterImpl
    }
}