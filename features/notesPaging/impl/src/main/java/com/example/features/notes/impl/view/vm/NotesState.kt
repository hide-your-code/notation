package com.example.features.notes.impl.view.vm

import androidx.compose.runtime.Stable
import com.example.core.common.view.viewmodel.ViewState
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class NotesState(
    val isLoading: Boolean = true,
) : ViewState
