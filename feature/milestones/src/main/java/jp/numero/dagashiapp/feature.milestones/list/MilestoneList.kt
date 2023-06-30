package jp.numero.dagashiapp.feature.milestones.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.navigation.navigate
import jp.numero.dagashiapp.model.Milestone
import jp.numero.dagashiapp.model.MilestoneList
import jp.numero.dagashiapp.navigation.destinations.SettingsScreenDestination
import jp.numero.dagashiapp.ui.R
import jp.numero.dagashiapp.ui.UiState
import jp.numero.dagashiapp.ui.component.*
import jp.numero.dagashiapp.ui.dateTimeString
import java.time.Instant

@Composable
fun MilestoneListScreen(
    navController: NavHostController,
    onClickMilestone: (Milestone) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    viewModel: MilestoneListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    MilestoneListScreen(
        uiState = uiState,
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
        onClickSettings = {
            navController.navigate(SettingsScreenDestination)
        },
        listState = listState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneListScreen(
    uiState: UiState<MilestoneList>,
    onClickMilestone: (Milestone) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onReachedBottom: () -> Unit,
    onClickSettings: () -> Unit,
    listState: LazyListState = rememberLazyListState(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                isCenterAlignedTitle = true,
                actions = {
                    IconButton(onClick = onClickSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
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
                        SwipeRefresh(
                            state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
                            onRefresh = onRefresh,
                        ) {
                            MilestoneListContent(
                                milestoneList = data,
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
fun MilestoneListContent(
    milestoneList: MilestoneList,
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
        contentPadding = WindowInsets.safeDrawing
            .only(WindowInsetsSides.Bottom)
            .asPaddingValues()
    ) {
        itemsIndexed(
            items = milestoneList.value,
            key = { _, item ->
                item.id
            },
            contentType = { _, _ ->
                MilestoneListContentType.Item
            }
        ) { _, item ->
            MilestoneItem(
                milestone = item,
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        Text(
            text = "#${milestone.number}",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = milestone.description,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = dateTimeString(
                instant = milestone.closedAd,
                format = stringResource(id = R.string.date_format)
            ),
            style = MaterialTheme.typography.labelSmall,
            color = LocalContentColor.current.copy(alpha = 0.54f)
        )
    }
}

@Preview("MilestoneItem")
@Composable
fun PreviewMilestoneItem() {
    MilestoneItem(
        milestone = Milestone(
            id = "id",
            number = 100,
            description = "Description",
            path = "",
            closedAd = Instant.now()
        ),
        onClick = {}
    )
}