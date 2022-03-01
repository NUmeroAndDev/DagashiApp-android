package jp.numero.dagashiapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.activity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import jp.takuji31.compose.navigation.screen.ScreenNavController
import jp.takuji31.compose.navigation.screen.ScreenNavHost

@Composable
fun DagashiNavigation(
    navController: ScreenNavController,
    homeScreen: @Composable () -> Unit,
    milestoneDetailScreen: @Composable () -> Unit,
    settingsScreen: @Composable () -> Unit,
) {
    val context = LocalContext.current
    ScreenNavHost(
        navController = navController,
        startScreen = Screen.Home,
    ) {
        screenComposable {
            home {
                homeScreen()
            }
            milestoneDetail {
                milestoneDetailScreen()
            }
            settings {
                settingsScreen()
            }
        }
        activity(
            route = Screen.Licenses.route,
        ) {
            OssLicensesMenuActivity.setActivityTitle(context.getString(R.string.licenses))
            activityClass = OssLicensesMenuActivity::class
        }
    }
}