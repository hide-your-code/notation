package com.example.core.common.view.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.model.Note

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onClick: (Note) -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(8.dp),
        onClick = {
            onClick(note)
        },
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = note.title,
            )

            Text(
                text = note.content,
            )
        }
    }
}
