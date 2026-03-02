package com.example.domain.domain

import com.example.core.model.Note
import com.example.domain.repositories.NoteRepository
import javax.inject.Inject
import kotlin.random.Random

interface AddNoteUseCase {
    suspend operator fun invoke(title: String, content: String): Result<Note>
}

class AddNoteUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository,
) : AddNoteUseCase {
    override suspend fun invoke(title: String, content: String): Result<Note> = runCatching {
        val title = title.trim().ifBlank {
            throw Throwable("Title must not be blank!")
        }
        val content = content.trim()

        val note = Note(
            id = Random.nextLong(),
            title = title,
            content = content,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        )

        noteRepository.addNote(note)
        return@runCatching note
    }
}
