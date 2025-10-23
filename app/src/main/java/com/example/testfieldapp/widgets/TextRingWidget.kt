package com.example.testfieldapp.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import com.example.testfieldapp.viewmodel.MainViewModel
import androidx.compose.runtime.getValue
import kotlin.math.absoluteValue

@Composable
fun TextRingWidget(
    viewModel: MainViewModel,
    modifier: Modifier,
    onClick: () -> Unit,
    noteName: String,
    freqDiff: Int?
) {
    val freqText = freqDiff?.toString() ?: "-"

    val borderColor by animateColorAsState(
        when {
            freqDiff == null -> Color.White
            freqDiff.absoluteValue <= 0.5 -> Color.Green
            freqDiff.absoluteValue > 0.5 && freqDiff.absoluteValue < 3 -> Color.Yellow

            else -> Color.Red
        },
        animationSpec = spring(
            stiffness = Spring.StiffnessVeryLow,
        )
    )

    ElevatedButton(
        onClick = onClick,
        modifier = modifier.size(250.dp),
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        border = BorderStroke(5.dp, borderColor)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(noteName, fontWeight = FontWeight.Bold, fontSize = 36.sp)
            Text(freqText)
        }
    }
}