package com.anasini.nuvodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.anasini.nuvodo.di.ViewModelFactory
import com.anasini.nuvodo.ui.archive.ArchiveScreen
import com.anasini.nuvodo.ui.archive.ArchiveViewModel
import com.anasini.nuvodo.ui.dashboard.DashboardScreen
import com.anasini.nuvodo.ui.dashboard.DashboardViewModel
import com.anasini.nuvodo.ui.theme.NuvodoTheme

sealed class Screen {
    data object Dashboard : Screen()
    data object Archive : Screen()
}

class MainActivity : ComponentActivity() {

    private val viewModelFactory: ViewModelFactory by lazy {
        val app = application as NuvodoApplication
        ViewModelFactory(app.appContainer)
    }

    private val dashboardViewModel: DashboardViewModel by viewModels { viewModelFactory }
    private val archiveViewModel: ArchiveViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            NuvodoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NuvodoApp(
                        dashboardViewModel = dashboardViewModel,
                        archiveViewModel = archiveViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NuvodoApp(
    dashboardViewModel: DashboardViewModel,
    archiveViewModel: ArchiveViewModel
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

    BackHandler(enabled = currentScreen == Screen.Archive) {
        currentScreen = Screen.Dashboard
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            if (targetState == Screen.Archive) {
                (slideInHorizontally(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    initialOffsetX = { fullWidth -> fullWidth }
                ) + fadeIn(spring())).togetherWith(
                    slideOutHorizontally(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        targetOffsetX = { fullWidth -> -fullWidth / 3 }
                    ) + fadeOut(spring())
                )
            } else {
                (slideInHorizontally(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    initialOffsetX = { fullWidth -> -fullWidth / 3 }
                ) + fadeIn(spring())).togetherWith(
                    slideOutHorizontally(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        targetOffsetX = { fullWidth -> fullWidth }
                    ) + fadeOut(spring())
                )
            }
        },
        label = "screenTransition"
    ) { screen ->
        when (screen) {
            Screen.Dashboard -> {
                DashboardScreen(
                    viewModel = dashboardViewModel,
                    onNavigateToArchive = { currentScreen = Screen.Archive }
                )
            }
            Screen.Archive -> {
                ArchiveScreen(
                    viewModel = archiveViewModel,
                    onNavigateBack = { currentScreen = Screen.Dashboard }
                )
            }
        }
    }
}
