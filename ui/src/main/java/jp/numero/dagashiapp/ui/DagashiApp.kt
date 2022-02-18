package jp.numero.dagashiapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jp.numero.dagashiapp.ui.settings.SettingsScreen
import jp.numero.dagashiapp.ui.theme.DagashiAppTheme
import jp.takuji31.compose.navigation.screen.ScreenNavHost
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
                ScreenNavHost(
                    navController = navController,
                    startScreen = Screen.Home,
                ) {
                    screenComposable {
                        home {
                            BoxWithConstraints {
                                if (isLargeScreen) {
                                    MilestoneListWithDetailScreen(navController)
                                } else {
                                    MilestoneListScreen(navController)
                                }
                            }
                        }
                        milestoneDetail {
                            MilestoneDetailScreen(navController)
                        }
                        settings {
                            SettingsScreen(navController)
                        }
                    }
                }
            }
        }
    }
}