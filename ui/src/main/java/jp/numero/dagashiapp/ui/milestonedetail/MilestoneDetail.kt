package jp.numero.dagashiapp.ui.milestonedetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import jp.numero.dagashiapp.model.Issue
import jp.numero.dagashiapp.model.Label
import jp.numero.dagashiapp.model.MilestoneDetail
import jp.numero.dagashiapp.ui.R
import jp.numero.dagashiapp.ui.UiState
import jp.numero.dagashiapp.ui.component.ErrorMessage
import jp.numero.dagashiapp.ui.component.FullScreenLoadingIndicator
import jp.numero.dagashiapp.ui.component.IssueDescriptionText
import jp.numero.dagashiapp.ui.component.TopAppBar

@Composable
fun MilestoneDetailScreen(
    navController: NavHostController
) {
    val viewModel: MilestoneDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current
    MilestoneDetailScreen(
        uiState = uiState,
        onBack = {
            navController.popBackStack()
        },
        onClickShare = {
            uriHandler.openUri(it)
        },
        onRetry = {
            // TODO: retry
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneDetailScreen(
    uiState: UiState<MilestoneDetail>,
    onBack: () -> Unit,
    onClickShare: (String) -> Unit,
    onRetry: () -> Unit,
) {
    val state = rememberTopAppBarState()
    val scrollBehavior = remember {
        TopAppBarDefaults.pinnedScrollBehavior(state)
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    uiState.data?.number?.let {
                        Text(text = "#$it")
                    }
                },
                isCenterAlignedTitle = false,
                contentPadding = WindowInsets.statusBars
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                    .asPaddingValues(),
                scrollBehavior = scrollBehavior,
                onBack = onBack,
                actions = {
                    uiState.data?.url?.let {
                        IconButton(
                            onClick = {
                                onClickShare(it)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = stringResource(id = R.string.share)
                            )
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                    loaded = { data, _ ->
                        MilestoneDetailContent(milestoneDetail = data)
                    },
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneDetailContent(
    milestoneDetail: MilestoneDetail,
    modifier: Modifier = Modifier,
    onClickInnerShare: ((String) -> Unit)? = null,
    applyFullInsets: Boolean = false,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        OutlinedCard(
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.systemBars
                    .let {
                        if (applyFullInsets) {
                            it
                        } else {
                            it.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
                        }
                    }
                    .add(
                        WindowInsets(
                            left = 16.dp,
                            top = 16.dp,
                            right = 16.dp,
                            bottom = 16.dp
                        )
                    )
            )
        ) {
            if (onClickInnerShare != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 12.dp
                        )
                ) {
                    Text(
                        text = "#${milestoneDetail.number}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    IconButton(
                        onClick = {
                            onClickInnerShare(milestoneDetail.url)
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = stringResource(id = R.string.share)
                        )
                    }
                }
            }
            milestoneDetail.issues.forEachIndexed { index, issue ->
                IssueItem(issue = issue)
                if (index != milestoneDetail.issues.lastIndex) {
                    Divider(startIndent = 16.dp)
                }
            }
        }
    }
}

@Composable
fun IssueItem(
    issue: Issue,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = issue.title,
            style = MaterialTheme.typography.titleMedium
        )
        if (issue.labels.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                issue.labels.forEachIndexed { index, label ->
                    IssueLabel(label)
                    if (index < issue.labels.lastIndex) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        IssueDescriptionText(text = issue.body)
        if (issue.comments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Comments(commentList = issue.comments)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueLabel(
    label: Label,
    modifier: Modifier = Modifier
) {
    val color = android.graphics.Color.parseColor("#${label.color}")
    Surface(
        modifier = modifier,
        color = Color(color),
        contentColor = color.contentColor(),
        shape = RoundedCornerShape(percent = 50)
    ) {
        Text(
            text = label.name,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * @return Color.Black or Color.White
 */
private fun Int.contentColor(): Color {
    val red = android.graphics.Color.red(this)
    val green = android.graphics.Color.green(this)
    val blue = android.graphics.Color.blue(this)
    return if (red * 0.299 + green * 0.587 + blue * 0.114 > 186) {
        Color.Black
    } else {
        Color.White
    }
}