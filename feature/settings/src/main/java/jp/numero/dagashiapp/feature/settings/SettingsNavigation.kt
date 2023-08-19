package jp.numero.dagashiapp.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val settingsRoute = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(settingsRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onBack: () -> Unit,
    onClickLicenses: () -> Unit,
) {
    composable(route = settingsRoute) {
        SettingsScreen(onBack, onClickLicenses)
    }
}
