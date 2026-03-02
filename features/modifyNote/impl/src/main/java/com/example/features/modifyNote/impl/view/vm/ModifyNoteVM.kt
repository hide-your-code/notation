package com.example.features.modifyNote.impl.view.vm

import androidx.lifecycle.viewModelScope
import com.example.core.common.view.viewmodel.CommonVM
import com.example.domain.domain.AddNoteUseCase
import com.example.domain.domain.UpdateNoteUseCase
import com.example.domain.repositories.NoteRepository
import com.example.features.modifyNote.api.ModifyNoteKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ModifyNoteVM.Factory::class)
class ModifyNoteVM @AssistedInject constructor(
    @Assisted private val navKey: ModifyNoteKey,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val noteRepository: NoteRepository,
) : CommonVM<ModifyNoteState, ModifyNoteAction, ModifyNoteEvent>() {
    override fun initState(): ModifyNoteState {
        return ModifyNoteState()
    }

    override fun handleAction(action: ModifyNoteAction) {
        when (action) {
            is ModifyNoteAction.Init -> initData()
            is ModifyNoteAction.Save -> handleSaveNote()
            is ModifyNoteAction.Delete -> handleDeleteNote()
            is ModifyNoteAction.OnTitleChanged -> handleOnTitleChanged(action.title)
            is ModifyNoteAction.OnContentChanged -> handleOnContentChanged(action.content)
        }
    }

    private fun handleDeleteNote() {
        viewModelScope.launch {
            val noteId = state.value.id ?: return@launch
            noteRepository.deleteNoteById(noteId)
            _event.send(ModifyNoteEvent.NoteDeletedSuccessful(noteId))
        }
    }

    private fun initData() {
        val noteId = navKey.id ?: return
        viewModelScope.launch {
            val note = noteRepository.getNoteById(noteId) ?: return@launch
            _state.update { it.copy(id = note.id) }
            launch { _event.send(ModifyNoteEvent.UpdateTitle(note.title)) }
            launch { _event.send(ModifyNoteEvent.UpdateContent(note.content)) }
        }
    }

    private fun handleOnContentChanged(content: String) {
        _state.update { it.copy(content = content) }
    }

    private fun handleOnTitleChanged(title: String) {
        _state.update { it.copy(title = title) }
    }

    private fun handleSaveNote() {
        val noteId = navKey.id
        if (noteId == null) {
            handleAddNote()
        } else {
            handleUpdateNote(noteId)
        }
    }

    private fun handleAddNote() {
        viewModelScope.launch {
            val currentState = state.value
            addNoteUseCase(title = currentState.title, content = currentState.content).onSuccess {
                _event.send(ModifyNoteEvent.NoteSavedSuccessful(it.id))
            }.onFailure {
                _event.send(ModifyNoteEvent.NoteSavedFailure(it.message.orEmpty()))
            }
        }
    }

    private fun handleUpdateNote(noteId: Long) {
        viewModelScope.launch {
            val currentState = state.value
            updateNoteUseCase(
                id = noteId,
                title = currentState.title,
                content = currentState.content,
            ).onSuccess {
                _event.send(ModifyNoteEvent.NoteUpdatedSuccessful(it))
            }.onFailure {
                _event.send(ModifyNoteEvent.NoteUpdatedFailure(it.message.orEmpty()))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(navKey: ModifyNoteKey): ModifyNoteVM
    }
}
