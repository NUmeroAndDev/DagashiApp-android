package jp.numero.dagashiapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jp.numero.dagashiapp.navigation.DagashiNavigation
import jp.numero.dagashiapp.ui.milestonedetail.MilestoneDetailScreen
import jp.numero.dagashiapp.ui.milestonelist.MilestoneListScreen
import jp.numero.dagashiapp.ui.settings.SettingsScreen
import jp.numero.dagashiapp.ui.theme.DagashiAppTheme
import jp.takuji31.compose.navigation.screen.rememberScreenNavController

@Composable
fun DagashiApp() {
    val navController = rememberScreenNavController()
    DagashiAppTheme {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        SideEffect {
            systemUiController.setSystemBarsColor(
                Color.Transparent,
                darkIcons = useDarkIcons
            )
        }
        ProvideWindowInsets {
            Surface(
                color = MaterialTheme.colorScheme.background
            ) {
                DagashiNavigation(
                    navController = navController,
                    homeScreen = {
                        BoxWithConstraints {
                            if (isLargeScreen) {
                                MilestoneListWithDetailScreen(navController)
                            } else {
                                MilestoneListScreen(navController)
                            }
                        }
                    },
                    milestoneDetailScreen = {
                        MilestoneDetailScreen(navController)
                    },
                    settingsScreen = {
                        SettingsScreen(navController)
                    }
                )
            }
        }
    }
}