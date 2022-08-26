package id.dhuwit.core.setting.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.dhuwit.core.setting.user.SettingUser
import id.dhuwit.core.setting.user.SettingUserImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingModule {

    @Provides
    @Singleton
    fun provideSettingUser(@ApplicationContext context: Context): SettingUser {
        return SettingUserImpl(context)
    }

}