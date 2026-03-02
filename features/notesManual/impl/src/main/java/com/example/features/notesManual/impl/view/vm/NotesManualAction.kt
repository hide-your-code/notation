package com.example.features.notesManual.impl.view.vm

import com.example.core.common.view.viewmodel.ViewAction
import com.example.core.model.Note

sealed interface NotesManualAction : ViewAction {
    data object Init : NotesManualAction
    data object Refresh : NotesManualAction
    data class Delete(val id: Long) : NotesManualAction
    data class Update(val note: Note) : NotesManualAction
    data object LoadMore : NotesManualAction
}
