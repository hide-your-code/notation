package com.example.data.mapper

import com.example.core.model.Note
import com.example.data.local.room.NoteEntity

object NoteMapper {
    fun NoteEntity.toModel(): Note = Note(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )

    fun Note.toEntity(): NoteEntity = NoteEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}
