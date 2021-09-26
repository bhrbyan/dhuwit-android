package id.dhuwit.core.category.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.dhuwit.core.category.database.CategoryDao
import id.dhuwit.core.category.database.CategoryDatabase
import id.dhuwit.core.category.repository.CategoryDataSource
import id.dhuwit.core.category.repository.CategoryLocalDataSource
import id.dhuwit.core.category.repository.CategoryRepository
import id.dhuwit.core.di.LocalSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoryRepositoryModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(@LocalSource local: CategoryDataSource): CategoryDataSource {
        return CategoryRepository(local)
    }

    @Provides
    @LocalSource
    fun provideCategoryLocalDataSource(dao: CategoryDao): CategoryDataSource {
        return CategoryLocalDataSource(dao)
    }

    @Provides
    @Singleton
    fun provideCategoryDao(@ApplicationContext context: Context): CategoryDao {
        return CategoryDatabase.getInstance(context).categoryDao()
    }

}