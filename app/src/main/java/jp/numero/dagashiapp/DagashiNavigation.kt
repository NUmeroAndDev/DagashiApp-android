package jp.numero.dagashiapp

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import jp.numero.dagashiapp.feature.licenses.licensesScreen
import jp.numero.dagashiapp.feature.licenses.navigateToLicenses
import jp.numero.dagashiapp.feature.milestones.milestonesRoute
import jp.numero.dagashiapp.feature.milestones.milestonesScreen
import jp.numero.dagashiapp.feature.settings.navigateToSettings
import jp.numero.dagashiapp.feature.settings.settingsScreen

@Composable
fun DagashiNavigation(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = milestonesRoute,
    ) {
        milestonesScreen(
            windowSizeClass = windowSizeClass,
            onClickSettings = navController::navigateToSettings
        )
        settingsScreen(
            onBack = navController::popBackStack,
            onClickLicenses = navController::navigateToLicenses
        )
        licensesScreen(context)
    }
}