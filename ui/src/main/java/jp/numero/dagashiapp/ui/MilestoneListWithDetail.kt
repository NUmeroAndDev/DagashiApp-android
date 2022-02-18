package jp.numero.dagashiapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import jp.numero.dagashiapp.model.Milestone
import jp.numero.dagashiapp.ui.component.ErrorMessage
import jp.numero.dagashiapp.ui.component.FullScreenLoadingIndicator
import jp.takuji31.compose.navigation.screen.ScreenNavController

@Composable
fun MilestoneListWithDetailScreen(
    navController: ScreenNavController
) {
    val viewModel: MilestoneListWithDetailViewModel = hiltViewModel()
    val uiStates by viewModel.uiStates.collectAsState()
    MilestoneListWithDetailScreen(
        uiStates = uiStates,
        onClickMilestone = {
            viewModel.selectedMilestone(it)
        },
        onReachedMilestoneListBottom = {
            viewModel.loadListMore()
        },
    )
}

@Composable
fun MilestoneListWithDetailScreen(
    uiStates: MilestoneListWithDetailUiStates,
    onClickMilestone: (Milestone) -> Unit,
    onReachedMilestoneListBottom: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                rememberInsetsPaddingValues(
                    LocalWindowInsets.current.statusBars,
                    applyBottom = false,
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        uiStates.milestoneList.onState(
            initialLoading = {
                FullScreenLoadingIndicator()
            },
            initialFailed = {
                ErrorMessage(error = it)
            },
            loaded = { data, _ ->
                Box(
                    modifier = Modifier
                        .width(334.dp)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    MilestoneListContent(
                        milestoneList = data,
                        onClickMilestone = onClickMilestone,
                        onReachedBottom = onReachedMilestoneListBottom,
                        modifier = Modifier.matchParentSize()
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    uiStates.milestoneDetail.onState(
                        initialLoading = {
                            FullScreenLoadingIndicator()
                        },
                        initialFailed = {
                            ErrorMessage(error = it)
                        },
                        loaded = { data, _ ->
                            MilestoneDetailContent(
                                milestoneDetail = data
                            )
                        }
                    )
                }
            }
        )
    }
}