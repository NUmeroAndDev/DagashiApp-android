package jp.numero.dagashiapp.feature.milestones

import android.os.Parcelable
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import jp.numero.dagashiapp.feature.milestones.detail.MilestoneDetailScreen
import jp.numero.dagashiapp.feature.milestones.list.MilestoneListScreen
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MilestonesListDetailScreen(
    windowSizeClass: WindowSizeClass,
    onClickSettings: () -> Unit,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<DetailPath>()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigableListDetailPaneScaffold(
            navigator = navigator,
            listPane = {
                AnimatedPane {
                    MilestoneListScreen(
                        selectedPath = navigator.currentDestination?.contentKey?.path,
                        listState = listState,
                        onClickMilestone = {
                            scope.launch {
                                navigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    DetailPath(it.path)
                                )
                            }
                        },
                        onClickSettings = onClickSettings,
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    navigator.currentDestination?.contentKey?.let {
                        MilestoneDetailScreen(
                            path = it.path,
                            onBack = {
                                scope.launch {
                                    navigator.navigateBack()
                                }
                            },
                            isExpanded = !navigator.isListExpanded(),
                        )
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldNavigator<*>.isListExpanded() =
    scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

@Parcelize
data class DetailPath(val path: String) : Parcelable