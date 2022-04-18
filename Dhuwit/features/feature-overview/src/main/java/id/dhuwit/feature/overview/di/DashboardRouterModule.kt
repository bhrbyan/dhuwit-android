package id.dhuwit.feature.overview.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.overview.router.DashboardRouter
import id.dhuwit.feature.overview.router.DashboardRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object DashboardRouterModule {

    @Provides
    fun provideDashboardRouter(): DashboardRouter {
        return DashboardRouterImpl
    }
}