package jp.numero.dagashiapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jp.numero.dagashiapp.model.Theme
import jp.numero.dagashiapp.navigation.DagashiNavigation
import jp.numero.dagashiapp.ui.milestonedetail.MilestoneDetailScreen
import jp.numero.dagashiapp.ui.milestonelist.MilestoneListScreen
import jp.numero.dagashiapp.ui.settings.SettingsScreen
import jp.numero.dagashiapp.ui.theme.DagashiAppTheme

@Composable
fun DagashiApp() {
    val navController = rememberNavController()
    val sharedViewModel = hiltViewModel<SharedViewModel>()
    val config by sharedViewModel.config.collectAsState()
    val isDarkTheme = config.theme.isDarkTheme()
    DagashiAppTheme(
        isDarkTheme = isDarkTheme,
        dynamicColor = config.applyDynamicColor
    ) {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isDarkTheme
        SideEffect {
            systemUiController.setSystemBarsColor(
                Color.Transparent,
                darkIcons = useDarkIcons
            )
        }
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

@Composable
fun Theme.isDarkTheme(): Boolean {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    return when (this) {
        Theme.Light -> false
        Theme.Dark -> true
        Theme.FollowSystem -> isSystemInDarkTheme
    }
}