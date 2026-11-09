package com.silknotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.getLong
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Modifier
import androidx.compose.foundation.layout.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.silknotes.presentation.editor.EditorScreen
import com.silknotes.presentation.home.HomeScreen
import com.silknotes.presentation.settings.SettingsScreen
import com.silknotes.presentation.theme.SilkNotesTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val app: SilkNotesApplication by lazy { SilkNotesApplication.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilkNotesTheme {
                SilkNotesApp()
            }
        }
    }

    private fun enableEdgeToEdge() {
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = 0
        window.navigationBarColor = 0
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SilkNotesApp() {
    val navController = rememberNavController()
    val app = SilkNotesApplication.getInstance(LocalContext.current)
    var onboardingComplete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val complete = app.getOnboardingCompleteUseCase().first()
        onboardingComplete = complete
    }

    if (!onboardingComplete) {
        androidx.compose.material3.Text(
            text = "Welcome to SilkNotes",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            modifier = androidx.compose.foundation.layout.Modifier.fillMaxSize().padding(16.dp)
        )
        return
    }

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNoteClick = { noteId -> navController.navigate("editor/$noteId") },
                onNewNote = { folderId -> navController.navigate("editor/new/${folderId ?: 0}") },
                onOpenSettings = { navController.navigate("settings") }
            )
        }
        composable(
            route = "editor/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val noteId = backStackEntry.getLong()
            EditorScreen(noteId = noteId, onBackPress = { navController.popBackStack() })
        }
        composable(
            route = "editor/new/{folderId}",
            arguments = listOf(navArgument("folderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val folderId = backStackEntry.getLong()
            EditorScreen(noteId = -1, folderId = folderId, onBackPress = { navController.popBackStack() })
        }
        composable("settings") {
            SettingsScreen(onBackPress = { navController.popBackStack() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SilkNotesAppPreview() {
    SilkNotesTheme { SilkNotesApp() }
}