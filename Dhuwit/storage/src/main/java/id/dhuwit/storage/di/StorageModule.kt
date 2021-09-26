package id.dhuwit.storage.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.dhuwit.storage.Storage
import id.dhuwit.storage.StorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideAppSharedPref(@ApplicationContext context: Context): Storage {
        return StorageImpl(context)
    }

}