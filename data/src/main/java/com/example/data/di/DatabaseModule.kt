package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.room.NoteDao
import com.example.data.local.room.NotationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNotationDatabase(
        @ApplicationContext context: Context,
    ): NotationDatabase {
        return Room.databaseBuilder(
            context,
            NotationDatabase::class.java,
            "notation.db",
        ).build()
    }

    @Provides
    fun provideNoteDao(
        database: NotationDatabase,
    ): NoteDao = database.noteDao()
}
