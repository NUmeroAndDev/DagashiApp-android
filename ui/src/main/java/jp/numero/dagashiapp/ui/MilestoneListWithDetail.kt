package jp.numero.dagashiapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
    val uriHandler = LocalUriHandler.current
    MilestoneListWithDetailScreen(
        uiStates = uiStates,
        onClickMilestone = {
            viewModel.selectedMilestone(it)
        },
        onReachedMilestoneListBottom = {
            viewModel.loadListMore()
        },
        onClickSettings = {
            navController.navigate(Screen.Settings)
        },
        onClickShare = {
            uriHandler.openUri(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneListWithDetailScreen(
    uiStates: MilestoneListWithDetailUiStates,
    onClickMilestone: (Milestone) -> Unit,
    onReachedMilestoneListBottom: () -> Unit,
    onClickSettings: () -> Unit,
    onClickShare: (String) -> Unit,
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
                    Box(
                        modifier = Modifier
                            .zIndex(1f)
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                            .align(Alignment.TopCenter),
                        contentAlignment = Alignment.Center
                    ) {
                        MilestoneListTopBar(
                            onClickSettings = onClickSettings,
                        )
                    }
                    MilestoneListContent(
                        milestoneList = data,
                        onClickMilestone = onClickMilestone,
                        onReachedBottom = onReachedMilestoneListBottom,
                        modifier = Modifier
                            .matchParentSize()
                            .padding(top = 64.dp)
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
                                milestoneDetail = data,
                                onClickInnerShare = onClickShare
                            )
                        }
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MilestoneListTopBar(
    onClickSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .heightIn(min = 64.dp)
            .wrapContentSize(align = Alignment.Center),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = onClickSettings,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(id = R.string.settings),
                )
            }
        }
    }
}