package com.example.testfieldapp.widgets

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testfieldapp.model.UiNote
import com.example.testfieldapp.viewmodel.MainViewModel

@Composable
fun NoteListWidget(
    modifier: Modifier,
    viewModel: MainViewModel
) {
    val notes = viewModel.currentTune.collectAsStateWithLifecycle()
    val selectedNote = viewModel.selectedNote.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (note in notes.value.notes) {
            NoteWidget(
                note,
                selectedNote.value == note,
                Color.Cyan,
                onClick = {
                    viewModel.changeSelectedNote(note)
                })
        }
    }
}

@Composable
fun NoteWidget(
    note: UiNote,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) color else Color.Transparent
    val textColor = if (isSelected) Color.Black else Color.Cyan

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, Color.White),
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