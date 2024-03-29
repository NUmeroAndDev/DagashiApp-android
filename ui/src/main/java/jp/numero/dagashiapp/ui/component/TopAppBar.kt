package jp.numero.dagashiapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import jp.numero.dagashiapp.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: @Composable () -> Unit,
    isCenterAlignedTitle: Boolean,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onBack: (() -> Unit)? = null
) {
    val navigationIcon: @Composable () -> Unit = if (onBack != null) {
        {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_up),
                )
            }
        }
    } else {
        {}
    }
    val windowInsets = WindowInsets.safeDrawing
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent
    )
    if (isCenterAlignedTitle) {
        CenterAlignedTopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            scrollBehavior = scrollBehavior,
            modifier = modifier,
            windowInsets = windowInsets,
            colors = colors,
        )
    } else {
        TopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            scrollBehavior = scrollBehavior,
            modifier = modifier,
            windowInsets = windowInsets,
            colors = colors,
        )
    }
}