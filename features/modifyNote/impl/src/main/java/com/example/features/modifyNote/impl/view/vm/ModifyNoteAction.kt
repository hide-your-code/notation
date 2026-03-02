package com.example.features.modifyNote.impl.view.vm

import com.example.core.common.view.viewmodel.ViewAction

sealed interface ModifyNoteAction : ViewAction {
    data object Init : ModifyNoteAction
    data object Save : ModifyNoteAction

    data object Delete : ModifyNoteAction

    data class OnTitleChanged(val title: String) : ModifyNoteAction

    data class OnContentChanged(val content: String) : ModifyNoteAction
}
