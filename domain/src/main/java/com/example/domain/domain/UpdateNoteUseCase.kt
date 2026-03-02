package com.example.domain.domain

import com.example.core.model.Note
import com.example.domain.repositories.NoteRepository
import javax.inject.Inject

interface UpdateNoteUseCase {
    suspend operator fun invoke(id: Long, title: String, content: String): Result<Note>
}

class UpdateNoteUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository,
) : UpdateNoteUseCase {
    override suspend fun invoke(id: Long, title: String, content: String) = runCatching {
        val currentNote = noteRepository.getNoteById(id) ?: throw Throwable("Can't file the note with id: $id")

        val title = title.trim().ifBlank {
            throw Throwable("Title must not be blank!")
        }
        val content = content.trim()

        val newNote = currentNote.copy(
            title = title,
            content = content,
            updatedAt = System.currentTimeMillis(),
        )

        noteRepository.updateNote(newNote)
        newNote
    }
}
