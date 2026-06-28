package com.example.testfieldapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testfieldapp.model.GuitarTune
import com.example.testfieldapp.navigation.Router
import com.example.testfieldapp.viewmodel.MainViewModel
import com.example.testfieldapp.widgets.TuningBoxWidget

@Composable
fun GuitarTuningScreen(modifier: Modifier, viewModel: MainViewModel, navController: NavController) {

    val tunings = listOf(
        GuitarTune.StandardTuning(),
        GuitarTune.DropDTuning(),
        GuitarTune.DropCTuning()
    )

    val currentTuning = viewModel.currentTune.collectAsState()
    var selectedTuning by remember { mutableStateOf<GuitarTune?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(
                top = 16.dp,
                bottom = 114.dp,
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = modifier.fillMaxSize()
        ) {
            for (tuning in tunings) {
                TuningBoxWidget(
                    modifier, tuning, onClick = { tuning ->
                        selectedTuning = tuning
                    },
                    isSelected = if (selectedTuning == null) {
                        currentTuning.value.name == tuning.name
                    } else {
                        selectedTuning?.name == tuning.name
                    }
                )
            }
        }
        OutlinedButton(
            enabled = selectedTuning != null,
            modifier = modifier
                .requiredHeight(60.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            border = BorderStroke(2.dp, Color.White),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                if (selectedTuning != null) {
                    viewModel.changeSelectedTuning(selectedTuning ?: tunings.first())
                    navController.popBackStack(
                        route = Router.MainScreen.name,
                        inclusive = false
                    )
                }
            }
        ) {
            Text("Select")
        }
    }
}