package id.dhuwit.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteSource