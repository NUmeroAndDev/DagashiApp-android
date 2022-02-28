package jp.numero.dagashiapp.appwidget

import android.os.Build
import androidx.annotation.ColorRes
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.cornerRadius

@Composable
fun WidgetTheme(
    content: @Composable () -> Unit
) {
    val widgetColorScheme = WidgetColorScheme(
        background = colorResource(id = R.color.appwidget_background),
        innerBackground = colorResource(id = R.color.appwidget_inner_background),
        textPrimary = colorResource(id = R.color.appwidget_text_primary),
        textSecondary = colorResource(id = R.color.appwidget_text_secondary)
    )
    CompositionLocalProvider(
        LocalColorScheme provides widgetColorScheme,
        content = content
    )
}

@Composable
@ReadOnlyComposable
fun colorResource(@ColorRes id: Int): Color {
    val context = LocalContext.current
    return Color(context.resources.getColor(id, context.theme))

}

internal val LocalColorScheme = staticCompositionLocalOf<WidgetColorScheme> {
    error("No default schema")
}

object WidgetTheme {
    val colorScheme: WidgetColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current
}

@Stable
class WidgetColorScheme(
    background: Color,
    innerBackground: Color,
    textPrimary: Color,
    textSecondary: Color,
) {
    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var innerBackground by mutableStateOf(innerBackground, structuralEqualityPolicy())
        internal set
    var textPrimary by mutableStateOf(textPrimary, structuralEqualityPolicy())
        internal set
    var textSecondary by mutableStateOf(textSecondary, structuralEqualityPolicy())
        internal set
}

@Composable
fun GlanceModifier.appWidgetBackgroundRadius(): GlanceModifier {
    return if (Build.VERSION.SDK_INT >= 31) {
        this.cornerRadius(android.R.dimen.system_app_widget_background_radius)
    } else {
        this.cornerRadius(16.dp)
    }
}

@Composable
fun GlanceModifier.appWidgetInnerRadius(): GlanceModifier {
    return if (Build.VERSION.SDK_INT >= 31) {
        this.cornerRadius(android.R.dimen.system_app_widget_inner_radius)
    } else {
        this.cornerRadius(8.dp)
    }
}