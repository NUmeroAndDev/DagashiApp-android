package jp.numero.dagashiapp.feature.milestones

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import jp.numero.dagashiapp.feature.milestones.detail.MilestoneDetailScreen
import jp.numero.dagashiapp.feature.milestones.list.MilestoneListScreen
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MilestonesContainerScreen(
    windowSizeClass: WindowSizeClass,
    onClickSettings: () -> Unit,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<DetailPath>()
    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }
    val listState = rememberLazyListState()
    Surface {
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    MilestoneListScreen(
                        selectedPath = navigator.currentDestination?.content?.path,
                        listState = listState,
                        onClickMilestone = {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                DetailPath(it.path)
                            )
                        },
                        onClickSettings = onClickSettings,
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    navigator.currentDestination?.content?.let {
                        MilestoneDetailScreen(
                            path = it.path,
                            onBack = {
                                navigator.navigateBack()
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