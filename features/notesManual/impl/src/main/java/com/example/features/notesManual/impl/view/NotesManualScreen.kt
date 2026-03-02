package com.example.features.notesManual.impl.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.common.view.navigation.Navigator
import com.example.core.common.view.note.NoteItem
import com.example.core.common.view.result.event.LocalResultEventBus
import com.example.core.model.Note
import com.example.features.modifyNote.api.ModifyNoteKey
import com.example.features.notesManual.impl.view.vm.NotesManualAction
import com.example.features.notesManual.impl.view.vm.NotesManualState
import com.example.features.notesManual.impl.view.vm.NotesManualVM
import kotlinx.coroutines.launch

@Composable
fun NotesManualScreen(
    viewModel: NotesManualVM,
    navigator: Navigator,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendAction(NotesManualAction.Init)
    }

    val resultEventBus = LocalResultEventBus.current
    LaunchedEffect(resultEventBus) {
        launch {
            resultEventBus.getResultFlow<Long>("NoteSaved").collect {
                viewModel.sendAction(NotesManualAction.Refresh)
            }
        }

        launch {
            resultEventBus.getResultFlow<Long>("NoteDeleted").collect {
                viewModel.sendAction(NotesManualAction.Delete(it))
            }
        }

        launch {
            resultEventBus.getResultFlow<Note>("NoteUpdated").collect {
                viewModel.sendAction(NotesManualAction.Update(it))
            }
        }
    }

    NotesManualContainer(
        state = state,
        onNavigateToAddNote = {
            navigator.goTo(ModifyNoteKey())
        },
        onNavigateToModifyNote = {
            navigator.goTo(ModifyNoteKey(it.id))
        },
        onAction = viewModel::sendAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotesManualContainer(
    state: NotesManualState,
    onNavigateToAddNote: () -> Unit = {},
    onNavigateToModifyNote: (Note) -> Unit = {},
    onAction: (NotesManualAction) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Notes manual")
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
        NotesManualContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = state,
            onNavigateToModifyNote = onNavigateToModifyNote,
            onAction = onAction,
        )
    }
}

@Composable
private fun NotesManualContent(
    modifier: Modifier = Modifier,
    state: NotesManualState,
    onAction: (NotesManualAction) -> Unit = {},
    onNavigateToModifyNote: (Note) -> Unit = {},
) {
    val lazyListState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false

            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            val loadMoreThreshold = 5
            !state.isLoadingMore && (lastVisibleIndex >= totalItems - 1 - loadMoreThreshold)
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onAction(NotesManualAction.LoadMore)
        }
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        items(
            items = state.notesById.values.sortedByDescending { it.createdAt },
            key = { it.id },
        ) { note ->
            NoteItem(
                modifier = Modifier.fillMaxWidth(),
                note = note,
                onClick = onNavigateToModifyNote,
            )
        }

        if (state.isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f),
                    )
                }
            }
        }
    }
}
