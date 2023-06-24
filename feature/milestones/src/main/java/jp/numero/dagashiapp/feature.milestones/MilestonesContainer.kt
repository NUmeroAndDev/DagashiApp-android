package jp.numero.dagashiapp.feature.milestones

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    val isLargeScreen = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Expanded
    val isSplit = isLargeScreen && !expanded
    ListDetailLayout(
        isSplit = isSplit,
        isShowDetail = selectedPath != null,
        listContent = {
            MilestoneListScreen(
                navController = navController,
                onClickMilestone = {
                    viewModel.openDetail(it.path)
                }
            )
        },
        detailContent = {
            selectedPath?.let { path ->
                MilestoneDetailScreen(
                    path = path,
                    onBack = viewModel::closeDetail,
                    isExpanded = !isSplit,
                    onChangedExpanded = {
                        if (it) {
                            viewModel.expandDetail()
                        } else {
                            viewModel.collapseDetail()
                        }
                    },
                    enableExpand = isLargeScreen
                )
            }
        },
        enableSplit = isLargeScreen,
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(
                WindowInsets.safeDrawing
                    .only(WindowInsetsSides.Horizontal)
            )
    )
}

@Composable
private fun ListDetailLayout(
    isSplit: Boolean,
    isShowDetail: Boolean,
    listContent: @Composable () -> Unit,
    detailContent: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    enableSplit: Boolean = true,
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor
    ) {
        if (enableSplit) {
            BoxWithConstraints {
                val width = maxWidth
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.animateContentSize()) {
                        Box(
                            modifier = Modifier
                                .let {
                                    if (isSplit && isShowDetail) {
                                        it.width(width / 2)
                                    } else if (isShowDetail) {
                                        it.width(0.dp)
                                    } else {
                                        it.fillMaxWidth()
                                    }
                                }
                        ) {
                            listContent()
                        }
                    }
                    Box(
                        modifier = Modifier
                            .let {
                                if (!isSplit && isShowDetail) {
                                    it.fillMaxSize()
                                } else {
                                    it.requiredWidth(width / 2)
                                }
                            }
                    ) {
                        detailContent()
                    }
                }

            }
        } else {
            Crossfade(targetState = isShowDetail) {
                if (it) {
                    detailContent()
                } else {
                    listContent()
                }
            }
        }
    }
}