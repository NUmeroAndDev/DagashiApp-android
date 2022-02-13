package jp.numero.dagashiapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
            ScreenNavHost(
                navController = navController,
                startScreen = Screen.MilestoneList,
            ) {
                screenComposable {
                    milestoneList {
                        MilestoneListScreen()
                    }
                    milestoneDetail {
                        // TODO: Impl milestone detail screen
                    }
                }
            }
        }
    }
}