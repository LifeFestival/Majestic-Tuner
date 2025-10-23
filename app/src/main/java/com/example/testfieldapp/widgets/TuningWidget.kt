package com.example.testfieldapp.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testfieldapp.viewmodel.MainViewModel

@Composable
fun TuningWidget(
    modifier: Modifier,
    viewModel: MainViewModel,
    onClick: () -> Unit
) {
    val currentTuning = viewModel.currentTune.collectAsStateWithLifecycle()

    OutlinedButton(
        modifier = modifier
            .requiredHeight(60.dp)
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        border = BorderStroke(2.dp, Color.White),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick
    ) {
        Text(currentTuning.value.name)
    }
}