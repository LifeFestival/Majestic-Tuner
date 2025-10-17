package com.example.testfieldapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.testfieldapp.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: MainViewModel
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Counter was")
    }
}