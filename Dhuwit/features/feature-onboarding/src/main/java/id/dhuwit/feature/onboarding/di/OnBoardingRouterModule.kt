package id.dhuwit.feature.onboarding.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.onboarding.router.OnBoardingRouter
import id.dhuwit.feature.onboarding.router.OnBoardingRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object OnBoardingRouterModule {

    @Provides
    fun provideOnBoardingRouter(): OnBoardingRouter {
        return OnBoardingRouterImpl
    }

}