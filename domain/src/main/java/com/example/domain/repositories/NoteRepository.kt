package com.example.domain.repositories

import androidx.paging.PagingData
import com.example.core.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun notes(pageSize: Int): Flow<PagingData<Note>>

    suspend fun getFirstPage(limit: Int): List<Note>

    suspend fun addNotes(notes: List<Note>)

    suspend fun getNextPage(limit: Int, lastCreatedAt: Long): List<Note>

    suspend fun getNoteById(id: Long): Note?

    suspend fun addNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNoteById(id: Long)
}
