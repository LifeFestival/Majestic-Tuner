package com.example.testfieldapp.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextRingWidget(modifier: Modifier, onClick: () -> Unit, noteName: String, freqDiff: String) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier.size(250.dp),
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        border = BorderStroke(5.dp, Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(noteName, fontWeight = FontWeight.Bold, fontSize = 36.sp)
            Text(freqDiff)
        }
    }
}