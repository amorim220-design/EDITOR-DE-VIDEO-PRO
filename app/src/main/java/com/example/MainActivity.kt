package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.editor.EditorScreen
import com.example.ui.home.HomeScreen
import com.example.ui.theme.ProVideoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ProVideoTheme {
        AppNavigation()
      }
    }
  }
}

@Composable
fun AppNavigation() {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = "home") {
    composable("home") {
      HomeScreen(
        viewModel = viewModel(),
        onProjectClick = { projectId ->
          navController.navigate("editor/$projectId")
        }
      )
    }
    composable(
      route = "editor/{projectId}",
      arguments = listOf(navArgument("projectId") { type = NavType.LongType })
    ) { backStackEntry ->
      val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
      EditorScreen(
        projectId = projectId,
        viewModel = viewModel(),
        onBack = { navController.popBackStack() }
      )
    }
  }
}
