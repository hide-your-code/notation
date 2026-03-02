package com.example.features.modifyNote.impl.view.vm

import androidx.compose.runtime.Stable
import com.example.core.common.view.viewmodel.ViewState

@Stable
data class ModifyNoteState(
    val isLoading: Boolean = true,
    val id: Long? = null,
    val title: String = "",
    val content: String = "",
) : ViewState
