package com.example

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
  companion object {
    private const val TAG = "ProVideo_MainActivity"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(TAG, "onCreate started")

    // Setup global crash handler
    val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
      Log.e(TAG, "CRASH DETECTED in thread ${thread.name}", throwable)
      // We can't do much here to show UI because the app is dying, 
      // but logging it is better than nothing.
      defaultHandler?.uncaughtException(thread, throwable)
    }

    enableEdgeToEdge()
    try {
      setContent {
        ProVideoTheme {
          AppNavigation()
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, "Fatal error in setContent", e)
      setContent {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
          Text(
            text = "Ocorreu um erro inesperado:\n${e.message}\n\nVerifique o logcat para mais detalhes.",
            color = MaterialTheme.colorScheme.error
          )
        }
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
