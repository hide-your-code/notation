package com.example.features.notes.impl.view.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.core.common.view.viewmodel.CommonVM
import com.example.domain.repositories.NoteRepository
import com.example.features.notes.api.NotesPagingKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = NotesVM.Factory::class)
class NotesVM @AssistedInject constructor(
    @Assisted private val navKey: NotesPagingKey,
    private val noteRepository: NoteRepository,
) : CommonVM<NotesState, NotesAction, NotesEvent>() {
    val pager = noteRepository.notes(
        pageSize = 20,
    ).cachedIn(viewModelScope)

    override fun initState(): NotesState {
        return NotesState()
    }

    override fun handleAction(action: NotesAction) {
    }

    @AssistedFactory
    interface Factory {
        fun create(navKey: NotesPagingKey): NotesVM
    }
}
