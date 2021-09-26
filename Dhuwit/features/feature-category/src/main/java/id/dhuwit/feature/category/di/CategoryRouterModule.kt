package id.dhuwit.feature.category.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.category.router.CategoryRouter
import id.dhuwit.feature.category.router.CategoryRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object CategoryRouterModule {
    @Provides
    fun provideCategoryRouter(): CategoryRouter {
        return CategoryRouterImpl
    }
}