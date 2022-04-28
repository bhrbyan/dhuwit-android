package id.dhuwit.core.budget.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import id.dhuwit.core.budget.database.BudgetDao
import id.dhuwit.core.budget.database.BudgetDatabase
import id.dhuwit.core.budget.repository.BudgetDataSource
import id.dhuwit.core.budget.repository.BudgetLocalDataSource
import id.dhuwit.core.budget.repository.BudgetRepository
import id.dhuwit.core.di.LocalSource
import javax.inject.Singleton

@Module
@InstallIn(Singleton::class)
object BudgetRepositoryModule {

    @Provides
    fun provideBudgetRepository(@LocalSource local: BudgetDataSource): BudgetDataSource {
        return BudgetRepository(local)
    }

    @Provides
    @LocalSource
    fun provideBudgetLocalDataSource(dao: BudgetDao): BudgetDataSource {
        return BudgetLocalDataSource(dao)
    }

    @Provides
    fun provideBudgetDao(@ApplicationContext context: Context): BudgetDao {
        return BudgetDatabase.getInstance(context).budgetDao()
    }

}