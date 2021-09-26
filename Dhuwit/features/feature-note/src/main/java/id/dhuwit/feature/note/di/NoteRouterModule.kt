package id.dhuwit.feature.note.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import id.dhuwit.feature.note.router.NoteRouter
import id.dhuwit.feature.note.router.NoteRouterImpl

@Module
@InstallIn(ActivityComponent::class)
object NoteRouterModule {
    @Provides
    fun provideNoteRouter(): NoteRouter {
        return NoteRouterImpl
    }
}