package id.dhuwit.feature.main.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.main.router.MainRouter
import id.dhuwit.feature.main.router.MainRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    @Provides
    fun provideMainRouter(): MainRouter {
        return MainRouterImpl
    }

}