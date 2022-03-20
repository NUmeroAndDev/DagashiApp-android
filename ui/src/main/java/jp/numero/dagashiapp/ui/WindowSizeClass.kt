package jp.numero.dagashiapp.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator

enum class WindowSizeClass { Compact, Medium, Expanded }

@Composable
fun Activity.rememberWindowSizeClass(): WindowSizeClass {
    val windowSize = rememberWindowSize()
    val windowDpSize = with(LocalDensity.current) {
        windowSize.toDpSize()
    }
    return getWindowSizeClass(windowDpSize)
}

@Composable
private fun Activity.rememberWindowSize(): Size {
    val configuration = LocalConfiguration.current
    val windowMetrics = remember(configuration) {
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
    }
    return windowMetrics.bounds.toComposeRect().size
}

private fun getWindowSizeClass(windowDpSize: DpSize): WindowSizeClass = when {
    windowDpSize.width < 0.dp -> throw IllegalArgumentException("Dp value cannot be negative")
    windowDpSize.width < 600.dp -> WindowSizeClass.Compact
    windowDpSize.width < 840.dp -> WindowSizeClass.Medium
    else -> WindowSizeClass.Expanded
}