package jp.numero.dagashiapp.feature.milestones

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import jp.numero.dagashiapp.feature.milestones.detail.MilestoneDetailScreen
import jp.numero.dagashiapp.feature.milestones.list.MilestoneListScreen

@Composable
fun MilestonesContainerScreen(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    viewModel: MilestonesContainerViewModel = hiltViewModel()
) {
    val selectedPath by viewModel.selectedPath.collectAsState()
    val expanded by viewModel.expanded.collectAsState()
    BackHandler(enabled = selectedPath != null) {
        viewModel.closeDetail()
    }
    ListDetailLayout(
        isLargeScreen = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium,
        list = {
            MilestoneListScreen(
                navController = navController,
                onClickMilestone = {
                    viewModel.openDetail(it.path)
                }
            )
        },
        detail = selectedPath?.let { path ->
            {
                MilestoneDetailScreen(
                    path = path,
                    navController = navController
                )
            }
        },
        expanded = expanded,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ListDetailLayout(
    isLargeScreen: Boolean,
    list: @Composable () -> Unit,
    detail: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
) {
    // TODO: Implement animation
    Box(modifier = modifier) {
        if (isLargeScreen && !expanded) {
            Row(modifier = Modifier.matchParentSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    list()
                }
                detail?.let {
                    Box(modifier = Modifier.weight(1f)) {
                        it.invoke()
                    }
                }
            }
        } else {
            detail?.invoke() ?: list()
        }
    }
}