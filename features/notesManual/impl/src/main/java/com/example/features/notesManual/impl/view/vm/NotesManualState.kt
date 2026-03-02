package com.example.features.notesManual.impl.view.vm

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.core.common.view.viewmodel.ViewState
import com.example.core.model.Note

@Stable
data class NotesManualState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val notesById: SnapshotStateMap<Long, Note> = mutableStateMapOf(),
) : ViewState
