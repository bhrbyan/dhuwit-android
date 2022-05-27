package id.dhuwit.feature.calculator.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.calculator.router.CalculatorRouter
import id.dhuwit.feature.calculator.router.CalculatorRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object CalculatorRouterModule {

    @Provides
    fun provideCalculatorRouter(): CalculatorRouter {
        return CalculatorRouterImpl()
    }

}