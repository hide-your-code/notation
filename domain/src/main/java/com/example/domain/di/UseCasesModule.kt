package com.example.domain.di

import com.example.domain.domain.AddNoteUseCase
import com.example.domain.domain.AddNoteUseCaseImpl
import com.example.domain.domain.UpdateNoteUseCase
import com.example.domain.domain.UpdateNoteUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModule {

    @Binds
    abstract fun bindsAddNoteUseCase(impl: AddNoteUseCaseImpl): AddNoteUseCase

    @Binds
    abstract fun bindsUpdateNoteUseCase(impl: UpdateNoteUseCaseImpl): UpdateNoteUseCase
}
