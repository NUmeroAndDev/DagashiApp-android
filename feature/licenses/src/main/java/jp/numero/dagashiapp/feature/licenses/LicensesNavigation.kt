package jp.numero.dagashiapp.feature.licenses

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.activity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

const val licensesRoute = "licenses"

fun NavController.navigateToLicenses(navOptions: NavOptions? = null) {
    navigate(licensesRoute, navOptions)
}

fun NavGraphBuilder.licensesScreen(
    context: Context,
) {
    activity(
        route = licensesRoute,
    ) {
        OssLicensesMenuActivity.setActivityTitle(context.getString(R.string.licenses))
        activityClass = OssLicensesMenuActivity::class
    }
}
