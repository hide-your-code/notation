package com.example.features.modifyNote.impl.view.vm

import com.example.core.common.view.viewmodel.ViewEvent
import com.example.core.model.Note

sealed interface ModifyNoteEvent : ViewEvent {
    data class UpdateTitle(val title: String) : ModifyNoteEvent

    data class UpdateContent(val content: String) : ModifyNoteEvent

    data class NoteSavedSuccessful(val noteId: Long) : ModifyNoteEvent

    data class NoteSavedFailure(val message: String) : ModifyNoteEvent

    data class NoteDeletedSuccessful(val noteId: Long) : ModifyNoteEvent
    data class NoteUpdatedSuccessful(val note: Note) : ModifyNoteEvent
    data class NoteUpdatedFailure(val message: String) : ModifyNoteEvent
}
