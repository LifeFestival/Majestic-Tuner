package com.example.testfieldapp.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testfieldapp.model.UiNote
import com.example.testfieldapp.viewmodel.MainViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NoteListWidget(
    modifier: Modifier,
    viewModel: MainViewModel
) {
    val notes = viewModel.currentTune.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (note in notes.value.notes) {
            NoteWidget(
                viewModel,
                note,
                Color.Cyan,
                onClick = {
                    viewModel.changeSelectedNote(note)
                })
        }
    }
}

@Composable
fun NoteWidget(
    viewModel: MainViewModel,
    note: UiNote,
    color: Color,
    onClick: () -> Unit,
) {
    val selectedNote = viewModel.selectedNote.collectAsStateWithLifecycle()
    val completion = viewModel.completionState.collectAsStateWithLifecycle()

    var isCompleted: Boolean by remember { mutableStateOf(false) }

    if (completion.value != null && note.octave == completion.value?.octave && note.note.noteIndex == completion.value?.noteIndex) {
        isCompleted = true
    }

    val backgroundColor = if (selectedNote.value == note) color else Color.Transparent
    val textColor = if (selectedNote.value == note) Color.Black else Color.Cyan

    val borderColor by animateColorAsState(
        if (isCompleted) Color.Green else Color.White,
        animationSpec = spring(
            stiffness = Spring.StiffnessVeryLow
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, borderColor),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .size(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = textColor
            ),
            onClick = onClick
        ) {
            Text(note.note.symbol)
        }
        Text(note.note.musicName, modifier = Modifier.padding(vertical = 8.dp))
    }
}