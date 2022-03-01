package jp.numero.dagashiapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ramcosta.composedestinations.utils.composable
import jp.numero.dagashiapp.navigation.destinations.HomeScreenDestination
import jp.numero.dagashiapp.navigation.destinations.LicensesScreenDestination
import jp.numero.dagashiapp.navigation.destinations.MilestoneDetailScreenDestination
import jp.numero.dagashiapp.navigation.destinations.SettingsScreenDestination

@Composable
fun DagashiNavigation(
    navController: NavHostController,
    homeScreen: @Composable () -> Unit,
    milestoneDetailScreen: @Composable () -> Unit,
    settingsScreen: @Composable () -> Unit,
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route,
    ) {
        composable(HomeScreenDestination) {
            homeScreen()
        }
        composable(MilestoneDetailScreenDestination) { _, _ ->
            milestoneDetailScreen()
        }
        composable(SettingsScreenDestination) {
            settingsScreen()
        }
        activity(
            route = LicensesScreenDestination.route,
        ) {
            OssLicensesMenuActivity.setActivityTitle(context.getString(R.string.licenses))
            activityClass = OssLicensesMenuActivity::class
        }
    }
}