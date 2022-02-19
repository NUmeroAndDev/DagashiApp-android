package jp.numero.dagashiapp.ui

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.ui.unit.dp

val BoxWithConstraintsScope.isLargeScreen: Boolean
    get() = maxWidth >= 600.dp