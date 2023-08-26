package jp.numero.dagashiapp.appwidget

import android.os.Build
import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.cornerRadius

@Composable
@ReadOnlyComposable
fun colorResource(@ColorRes id: Int): Color {
    val context = LocalContext.current
    return Color(context.resources.getColor(id, context.theme))

}

@Composable
fun GlanceModifier.appWidgetBackgroundRadius(): GlanceModifier {
    return if (Build.VERSION.SDK_INT >= 31) {
        cornerRadius(android.R.dimen.system_app_widget_background_radius)
    } else {
        cornerRadius(16.dp)
    }
}

@Composable
fun GlanceModifier.appWidgetInnerRadius(): GlanceModifier {
    return if (Build.VERSION.SDK_INT >= 31) {
        cornerRadius(android.R.dimen.system_app_widget_inner_radius)
    } else {
        cornerRadius(8.dp)
    }
}