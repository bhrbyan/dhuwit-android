package id.dhuwit.feature.budget.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.budget.router.BudgetRouter
import id.dhuwit.feature.budget.router.BudgetRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object BudgetRouterModule {

    @Provides
    fun provideBudgetRouter(): BudgetRouter {
        return BudgetRouterImpl
    }
}