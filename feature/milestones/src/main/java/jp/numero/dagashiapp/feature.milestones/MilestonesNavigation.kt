package jp.numero.dagashiapp.feature.milestones

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val milestonesRoute = "milestones"

fun NavController.navigateToMilestones(navOptions: NavOptions? = null) {
    navigate(milestonesRoute, navOptions)
}

fun NavGraphBuilder.milestonesScreen(
    windowSizeClass: WindowSizeClass,
    onClickSettings: () -> Unit,
) {
    composable(route = milestonesRoute) {
        MilestonesListDetailScreen(
            windowSizeClass = windowSizeClass,
            onClickSettings = onClickSettings,
        )
    }
}
