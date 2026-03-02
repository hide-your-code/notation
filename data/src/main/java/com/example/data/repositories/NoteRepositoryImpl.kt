package com.example.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.core.model.Note
import com.example.data.local.room.NoteDao
import com.example.data.mapper.NoteMapper.toEntity
import com.example.data.mapper.NoteMapper.toModel
import com.example.domain.repositories.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
) : NoteRepository {
    override fun notes(pageSize: Int): Flow<PagingData<Note>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { noteDao.pagingSource() },
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toModel()
            }
        }
    }

    override suspend fun getFirstPage(limit: Int): List<Note> {
        return noteDao.firstPage(limit).map { it.toModel() }
    }

    override suspend fun getNextPage(limit: Int, lastCreatedAt: Long): List<Note> {
        return noteDao.nextPage(lastCreatedAt, limit).map { it.toModel() }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return noteDao.getById(id)?.toModel()
    }

    override suspend fun addNote(note: Note) {
        noteDao.insert(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.update(note.toEntity())
    }

    override suspend fun deleteNoteById(id: Long) {
        noteDao.deleteById(id)
    }

    override suspend fun addNotes(notes: List<Note>) {
        noteDao.insertAll(notes.map { it.toEntity() })
    }
}
