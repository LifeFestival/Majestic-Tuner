package com.example.testfieldapp.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.testfieldapp.model.UiNote
import com.example.testfieldapp.viewmodel.MainViewModel
import com.example.testfieldapp.widgets.InstrumentSelectionWidget
import com.example.testfieldapp.widgets.NoteListWidget
import com.example.testfieldapp.widgets.TextRingWidget
import com.example.testfieldapp.widgets.TuningWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: MainViewModel
) {
    var hasPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val isRecording = viewModel.isRecording.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            hasPermission = true
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val currentNote = viewModel.currentNote.collectAsStateWithLifecycle()
    val selectedNote = viewModel.selectedNote.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            InstrumentSelectionWidget()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier.weight(2f))
        TextRingWidget(
            viewModel,
            modifier.weight(8f, fill = false),
            noteName = "${currentNote.value?.note?.symbol ?: '-'}",
            freqDiff = calculateIndexDiff(
                currentNote.value,
                selectedNote.value
            ),
            onClick = {
                if (hasPermission) {
                    if (!isRecording.value) {
                        viewModel.startRecording()
                    } else {
                        viewModel.stopRecording()
                    }
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            },
        )
        Spacer(modifier.weight(1f))
        NoteListWidget(modifier.weight(1f), viewModel)
        TuningWidget(modifier.weight(2f), viewModel, {
            showBottomSheet = true
        })

    }
}

private fun calculateIndexDiff(currentNote: UiNote?, targetNote: UiNote): Int? {
    return if (currentNote == null) null
    else currentNote.note.noteIndex * currentNote.octave - targetNote.note.noteIndex * targetNote.octave
}