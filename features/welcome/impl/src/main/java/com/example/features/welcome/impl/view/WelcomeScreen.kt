package com.example.features.welcome.impl.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.common.view.navigation.Navigator
import com.example.features.notes.api.NotesPagingKey
import com.example.features.notesManual.api.NotesManualKey
import com.example.features.welcome.impl.view.vm.WelcomeState
import com.example.features.welcome.impl.view.vm.WelcomeVM

@Composable
fun WelcomeScreen(
    viewModel: WelcomeVM,
    navigator: Navigator,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WelcomeContainer(
        state = state,
        onNavigateToNotesPaging = {
            navigator.goTo(NotesPagingKey)
        },
        onNavigateToNotesManual = {
            navigator.goTo(NotesManualKey)
        }
    )
}

@Composable
private fun WelcomeContainer(
    state: WelcomeState,
    onNavigateToNotesPaging: () -> Unit = {},
    onNavigateToNotesManual: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        WelcomeContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onNavigateToNotesPaging = onNavigateToNotesPaging,
            onNavigateToNotesManual = onNavigateToNotesManual,
        )
    }
}

@Composable
private fun WelcomeContent(
    modifier: Modifier = Modifier,
    onNavigateToNotesPaging: () -> Unit = {},
    onNavigateToNotesManual: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Welcome to Notation")

        Spacer(modifier = Modifier.height(24.dp))

        Text("Select the mechanism to display the Notes")

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onNavigateToNotesManual()
            }
        ) {
            Text("Manual pagination")
        }

        Button(
            onClick = {
                onNavigateToNotesPaging()
            }
        ) {
            Text("Using Paging 3")
        }
    }
}
