package id.dhuwit.feature.overview.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.overview.router.OverviewRouter
import id.dhuwit.feature.overview.router.OverviewRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object OverviewRouterModule {

    @Provides
    fun provideOverviewRouter(): OverviewRouter {
        return OverviewRouterImpl
    }
}