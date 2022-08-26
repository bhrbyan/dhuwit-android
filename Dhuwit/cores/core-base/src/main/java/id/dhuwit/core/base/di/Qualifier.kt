package id.dhuwit.core.base.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteSource