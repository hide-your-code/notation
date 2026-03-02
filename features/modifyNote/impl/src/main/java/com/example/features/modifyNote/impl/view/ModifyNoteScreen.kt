package com.example.features.modifyNote.impl.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.common.view.navigation.Navigator
import com.example.core.common.view.result.event.LocalResultEventBus
import com.example.features.modifyNote.impl.view.vm.ModifyNoteAction
import com.example.features.modifyNote.impl.view.vm.ModifyNoteEvent
import com.example.features.modifyNote.impl.view.vm.ModifyNoteState
import com.example.features.modifyNote.impl.view.vm.ModifyNoteVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyNoteScreen(
    viewModel: ModifyNoteVM,
    navigator: Navigator,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val titleText = rememberTextFieldState("")
    val contentText = rememberTextFieldState("")

    val snackbarHostState = remember { SnackbarHostState() }

    val resultEventBus = LocalResultEventBus.current

    LaunchedEffect(Unit) {
        viewModel.sendAction(ModifyNoteAction.Init)
    }

    LaunchedEffect(titleText) {
        snapshotFlow { titleText.text.toString() }.collect {
            viewModel.sendAction(ModifyNoteAction.OnTitleChanged(it))
        }
    }

    LaunchedEffect(contentText) {
        snapshotFlow { contentText.text.toString() }.collect {
            viewModel.sendAction(ModifyNoteAction.OnContentChanged(it))
        }
    }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is ModifyNoteEvent.UpdateTitle -> {
                    titleText.setTextAndPlaceCursorAtEnd(event.title)
                }

                is ModifyNoteEvent.UpdateContent -> {
                    contentText.setTextAndPlaceCursorAtEnd(event.content)
                }

                is ModifyNoteEvent.NoteSavedSuccessful -> {
                    resultEventBus.sendResult("NoteSaved", event.noteId)
                    navigator.goBack()
                }

                is ModifyNoteEvent.NoteSavedFailure -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is ModifyNoteEvent.NoteDeletedSuccessful -> {
                    resultEventBus.sendResult("NoteDeleted", event.noteId)
                    navigator.goBack()
                }

                is ModifyNoteEvent.NoteUpdatedSuccessful -> {
                    resultEventBus.sendResult("NoteUpdated", event.note)
                    navigator.goBack()
                }

                is ModifyNoteEvent.NoteUpdatedFailure -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    ModifyNoteContainer(
        state = state,
        snackbarHostState = snackbarHostState,
        titleText = titleText,
        contentText = contentText,
        onAction = viewModel::sendAction,
        onBackPressed = {
            navigator.goBack()
        },
    )
}

@Composable
private fun ModifyNoteContainer(
    state: ModifyNoteState,
    snackbarHostState: SnackbarHostState,
    titleText: TextFieldState,
    contentText: TextFieldState,
    onBackPressed: () -> Unit = {},
    onAction: (ModifyNoteAction) -> Unit = {},
) {
    val scrollableState = rememberScrollState()

    val (focusRequester) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .clickable(
                enabled = true,
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                },
            ),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        },
        topBar = {
            TopBar(
                state = state,
                onBackPressed = onBackPressed,
                onAction = onAction,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(ModifyNoteAction.Save)
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save note",
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .scrollable(scrollableState, orientation = Orientation.Vertical)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Title(
                modifier = Modifier.fillMaxWidth(),
                titleText = titleText,
                onKeyboardAction = {
                    focusRequester.requestFocus()
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            Content(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                contentText = contentText,
                onKeyboardAction = {
                    keyboardController?.hide()
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    state: ModifyNoteState,
    onBackPressed: () -> Unit,
    onAction: (ModifyNoteAction) -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = if (state.id == null) "Add note" else "Modify note",
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackPressed()
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                )
            }
        },
        actions = {
            if (state.id != null) {
                IconButton(
                    onClick = {
                        onAction(ModifyNoteAction.Delete)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete note",
                    )
                }
            }
        },
    )
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    titleText: TextFieldState,
    onKeyboardAction: KeyboardActionHandler? = null,
) {
    OutlinedTextField(
        state = titleText,
        label = {
            Text(
                text = "Title",
            )
        },
        placeholder = {
            Text(
                text = "Input your note title",
            )
        },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        onKeyboardAction = onKeyboardAction,
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    contentText: TextFieldState,
    onKeyboardAction: KeyboardActionHandler? = null,
) {
    OutlinedTextField(
        state = contentText,
        label = {
            Text(
                text = "Content",
            )
        },
        placeholder = {
            Text(
                text = "Input your note content",
            )
        },
        modifier = modifier,
        onKeyboardAction = onKeyboardAction,
    )
}
