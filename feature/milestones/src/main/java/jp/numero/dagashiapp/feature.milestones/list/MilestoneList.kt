package jp.numero.dagashiapp.feature.milestones.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.numero.dagashiapp.data.Milestone
import jp.numero.dagashiapp.data.MilestoneList
import jp.numero.dagashiapp.ui.R
import jp.numero.dagashiapp.ui.UiDrawables
import jp.numero.dagashiapp.ui.UiState
import jp.numero.dagashiapp.ui.component.ErrorMessage
import jp.numero.dagashiapp.ui.component.FullScreenLoadingIndicator
import jp.numero.dagashiapp.ui.component.LoadingIndicatorItem
import jp.numero.dagashiapp.ui.component.TopAppBar
import jp.numero.dagashiapp.ui.dateTimeString
import java.time.Instant

@Composable
fun MilestoneListScreen(
    selectedPath: String?,
    onClickMilestone: (Milestone) -> Unit,
    onClickSettings: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    viewModel: MilestoneListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    MilestoneListScreen(
        uiState = uiState,
        selectedPath = selectedPath,
        onClickMilestone = onClickMilestone,
        onRetry = {
            // TODO: retry
        },
        onRefresh = {
            viewModel.refresh()
        },
        onReachedBottom = {
            viewModel.loadMore()
        },
        onClickSettings = onClickSettings,
        listState = listState,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MilestoneListScreen(
    uiState: UiState<MilestoneList>,
    selectedPath: String?,
    onClickMilestone: (Milestone) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onReachedBottom: () -> Unit,
    onClickSettings: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                isCenterAlignedTitle = true,
                actions = {
                    IconButton(onClick = onClickSettings) {
                        Icon(
                            painter = painterResource(UiDrawables.ic_settings),
                            contentDescription = stringResource(id = R.string.settings),
                        )
                    }
                }
            )
            Box(
                modifier = Modifier.weight(1f)
            ) {
                uiState.onState(
                    initialLoading = {
                        FullScreenLoadingIndicator()
                    },
                    initialFailed = {
                        ErrorMessage(
                            error = it,
                            modifier = Modifier.fillMaxSize(),
                            action = {
                                Button(
                                    onClick = { onRetry() }
                                ) {
                                    Text(text = stringResource(id = R.string.retry))
                                }
                            }
                        )
                    },
                    loaded = { data, error ->
                        val state = rememberPullToRefreshState()
                        PullToRefreshBox(
                            state = state,
                            isRefreshing = uiState.isRefreshing,
                            onRefresh = onRefresh,
                        ) {
                            MilestoneListContent(
                                milestoneList = data,
                                selectedPath = selectedPath,
                                onClickMilestone = onClickMilestone,
                                onReachedBottom = onReachedBottom,
                                listState = listState,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        if (error != null) {
                            LaunchedEffect(error) {
                                val result = snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.load_error_message),
                                    actionLabel = context.getString(R.string.retry)
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    onRetry()
                                }
                            }
                        }
                    },
                )
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun MilestoneListContent(
    milestoneList: MilestoneList,
    selectedPath: String?,
    onClickMilestone: (Milestone) -> Unit,
    onReachedBottom: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val currentOnReachedBottom by rememberUpdatedState(onReachedBottom)
    val isReachedBottom by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size == listState.layoutInfo.totalItemsCount
        }
    }
    LaunchedEffect(isReachedBottom) {
        snapshotFlow { isReachedBottom }
            .collect { isReached ->
                if (isReached) {
                    currentOnReachedBottom()
                }
            }
    }
    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = WindowInsets.safeDrawing
            .only(WindowInsetsSides.Bottom)
            .asPaddingValues()
    ) {
        items(
            items = milestoneList.value,
            key = {
                it.id
            },
            contentType = {
                MilestoneListContentType.Item
            }
        ) { item ->
            MilestoneItem(
                milestone = item,
                isSelected = selectedPath == item.path,
                onClick = {
                    onClickMilestone(item)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (milestoneList.hasMore) {
            item(
                key = "Indicator",
                contentType = MilestoneListContentType.Indicator
            ) {
                LoadingIndicatorItem()
            }
        }
    }
}

private enum class MilestoneListContentType {
    Item, Indicator
}

@Composable
fun MilestoneItem(
    milestone: Milestone,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    FilterChipDefaults.filterChipColors()
    Surface(
        onClick = onClick,
        modifier = modifier.padding(horizontal = 16.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${milestone.number}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = dateTimeString(
                        instant = milestone.closedAd,
                        format = stringResource(id = R.string.date_format)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = LocalContentColor.current.copy(alpha = 0.64f)
                )
            }
            milestone.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview("MilestoneItem")
@Composable
private fun PreviewMilestoneItem() {
    MilestoneItem(
        milestone = Milestone(
            id = "id",
            number = 100,
            description = "Description",
            path = "",
            closedAd = Instant.now(),
            issues = emptyList(),
        ),
        onClick = {}
    )
}