package com.example.notation.di

import com.example.core.common.view.navigation.Navigator
import com.example.features.notes.api.NotesPagingKey
import com.example.features.notesManual.api.NotesManualKey
import com.example.features.welcome.api.WelcomeKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNavigator(): Navigator = Navigator(startDestination = WelcomeKey)
}
