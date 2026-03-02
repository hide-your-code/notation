package com.example.features.notes.impl.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.core.common.view.navigation.Navigator
import com.example.core.common.view.note.NoteItem
import com.example.core.model.Note
import com.example.features.modifyNote.api.ModifyNoteKey
import com.example.features.notes.impl.view.vm.NotesState
import com.example.features.notes.impl.view.vm.NotesVM


@Composable
fun NotesScreen(
    viewModel: NotesVM,
    navigator: Navigator,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pager = viewModel.pager.collectAsLazyPagingItems()

    NotesContainer(
        state = state,
        pagingItems = pager,
        onNavigateToAddNote = {
            navigator.goTo(ModifyNoteKey())
        },
        onNavigateToModifyNote = { note ->
            navigator.goTo(ModifyNoteKey(note.id))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotesContainer(
    state: NotesState,
    pagingItems: LazyPagingItems<Note>,
    onNavigateToAddNote: () -> Unit = {},
    onNavigateToModifyNote: (Note) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Notes")
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToAddNote()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NoteAdd,
                    contentDescription = "Add note",
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.matchParentSize(),
                contentPadding = innerPadding,
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id },
                ) { index ->
                    when (val note = pagingItems[index]) {
                        null -> {
                            NotePlaceHolder(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                            )
                        }

                        else -> {
                            NoteItem(
                                modifier = Modifier.fillMaxWidth(),
                                note = note,
                                onClick = onNavigateToModifyNote,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotePlaceHolder(
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        CircularProgressIndicator()
    }
}
