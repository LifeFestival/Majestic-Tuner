package com.example.testfieldapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testfieldapp.screens.MainScreen
import com.example.testfieldapp.screens.SettingsScreen
import com.example.testfieldapp.screens.GuitarTuningScreen
import com.example.testfieldapp.viewmodel.MainViewModel

@Composable
fun Navigation(modifier: Modifier) {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()

    NavHost(navController = navController, startDestination = Router.MainScreen.name) {
        composable(route = Router.MainScreen.name) {
            MainScreen(modifier = modifier, navController, viewModel)
        }
        composable(route = Router.SettingsScreen.name) {
            SettingsScreen(modifier, navController, viewModel)
        }
        composable(route = Router.GuitarTuningScreen.name) {
            GuitarTuningScreen(modifier, viewModel, navController)
        }
    }
}

enum class Router {
    MainScreen,
    SettingsScreen,
    GuitarTuningScreen,
}