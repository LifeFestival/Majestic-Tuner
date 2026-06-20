package com.example.testfieldapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testfieldapp.model.GuitarTune
import com.example.testfieldapp.viewmodel.MainViewModel
import com.example.testfieldapp.widgets.TuningBoxWidget

@Composable
fun GuitarTuningScreen(modifier: Modifier, viewModel: MainViewModel) {

    val tunings = listOf(
        GuitarTune.StandardTuning(),
        GuitarTune.DropDTuning(),
        GuitarTune.DropCTuning()
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier =  modifier.padding(
        vertical = 30.dp,
        horizontal = 16.dp).fillMaxSize()
    ) {
        for (tuning in tunings) {
            TuningBoxWidget(modifier, tuning, viewModel)
        }
    }
}