package com.example.features.notesManual.impl.view.vm

import androidx.lifecycle.viewModelScope
import com.example.core.common.view.viewmodel.CommonVM
import com.example.core.model.Note
import com.example.domain.repositories.NoteRepository
import com.example.features.notesManual.api.NotesManualKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@HiltViewModel(assistedFactory = NotesManualVM.Factory::class)
class NotesManualVM @AssistedInject constructor(
    @Assisted private val navKey: NotesManualKey,
    private val noteRepository: NoteRepository,
) : CommonVM<NotesManualState, NotesManualAction, NotesManualEvent>() {
    private var isInitialized: Boolean = false
    private var canLoadMore = true
    private var lastCreatedAt = Long.MAX_VALUE
    private var isRequestInFlight = false

    override fun initState(): NotesManualState {
        return NotesManualState()
    }

    override fun handleAction(action: NotesManualAction) {
        when (action) {
            is NotesManualAction.Init -> initData()
            is NotesManualAction.Refresh -> handleRefreshData()
            is NotesManualAction.Delete -> handleDeleteNote(action.id)
            is NotesManualAction.LoadMore -> handleLoadMore()
            is NotesManualAction.Update -> handleUpdateNote(action.note)
        }
    }

    private fun handleUpdateNote(note: Note) {
        state.value.notesById[note.id] = note
    }

    private fun handleDeleteNote(id: Long) {
        state.value.notesById.remove(id)
    }

    private fun handleRefreshData() {
        if (isRequestInFlight) return
        viewModelScope.launch {
            isRequestInFlight = true
            _state.update { it.copy(isLoading = true) }
            try {
                val notes = noteRepository.getFirstPage(PAGE_SIZE)
                val notesById = notes.associateBy { it.id }
                state.value.notesById.putAll(notesById)

                lastCreatedAt = notes.lastOrNull()?.createdAt ?: Long.MAX_VALUE
                canLoadMore = notes.size == PAGE_SIZE
            } finally {
                isRequestInFlight = false
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun handleLoadMore() {
        if (!canLoadMore) return
        if (isRequestInFlight) return
        viewModelScope.launch {
            isRequestInFlight = true
            _state.update { it.copy(isLoadingMore = true) }
            delay(3.seconds)
            try {
                val nextNotes = noteRepository.getNextPage(PAGE_SIZE, lastCreatedAt)

                if (nextNotes.isNotEmpty()) {
                    lastCreatedAt = nextNotes.last().createdAt

                    val nextNotesById = nextNotes.associateBy { it.id }
                    state.value.notesById.putAll(nextNotesById)
                }

                canLoadMore = nextNotes.size == PAGE_SIZE
            } finally {
                isRequestInFlight = false
                _state.update { it.copy(isLoadingMore = false) }
            }
        }
    }

    private fun initData() {
        if (isInitialized) return
        isInitialized = true

        if (isRequestInFlight) return
        viewModelScope.launch {
            isRequestInFlight = true
            _state.update { it.copy(isLoading = true) }
            try {
                val notes = noteRepository.getFirstPage(PAGE_SIZE)
                val notesById = notes.associateBy { it.id }
                state.value.notesById.clear()
                state.value.notesById.putAll(notesById)

                if (notes.isNotEmpty()) {
                    lastCreatedAt = notes.last().createdAt
                }

                canLoadMore = notes.size == PAGE_SIZE
            } finally {
                isRequestInFlight = false
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(navKey: NotesManualKey): NotesManualVM
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
