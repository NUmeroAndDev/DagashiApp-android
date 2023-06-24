package jp.numero.dagashiapp.feature.milestones.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloseFullscreen
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    path: String,
    onBack: () -> Unit,
    isExpanded: Boolean,
    onChangedExpanded: (Boolean) -> Unit,
    enableExpand: Boolean = false,
    viewModel: MilestoneDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current
    LaunchedEffect(path) {
        viewModel.load(path)
    }
    MilestoneDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onClickShare = {
            uriHandler.openUri(it)
        },
        onRetry = {
            // TODO: retry
        },
        isExpanded = isExpanded,
        onChangedExpanded = onChangedExpanded,
        enableExpand = enableExpand
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneDetailScreen(
    uiState: UiState<MilestoneDetail>,
    onBack: () -> Unit,
    onClickShare: (String) -> Unit,
    onRetry: () -> Unit,
    isExpanded: Boolean,
    onChangedExpanded: (Boolean) -> Unit,
    enableExpand: Boolean = false,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = if (isExpanded) {
            RoundedCornerShape(percent = 0)
        } else {
            MaterialTheme.shapes.extraLarge
        },
        modifier = Modifier.fillMaxSize()
            .let {
                if (isExpanded) {
                    it
                } else {
                    it
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing
                                .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                        )
                        .padding(16.dp)
                }
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    uiState.data?.number?.let {
                        Text(text = "#$it")
                    }
                },
                isCenterAlignedTitle = false,
                onBack = onBack,
                actions = {
                    if (enableExpand) {
                        ToggleExpandButton(
                            isExpanded = isExpanded,
                            onChanged = onChangedExpanded
                        )
                    }
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
            Box(modifier = Modifier.weight(1f)) {
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
                        MilestoneDetailContent(
                            milestoneDetail = data,
                            isExpanded = isExpanded,
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun MilestoneDetailContent(
    milestoneDetail: MilestoneDetail,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .let {
                if (isExpanded) {
                    it.windowInsetsPadding(
                        WindowInsets.safeDrawing
                            .only(WindowInsetsSides.Bottom)
                    )
                } else {
                    it
                }
            }
    ) {
        milestoneDetail.issues.forEachIndexed { index, issue ->
            IssueItem(issue = issue)
            if (index != milestoneDetail.issues.lastIndex) {
                Divider(modifier = Modifier.padding(start = 16.dp))
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

@Composable
private fun ToggleExpandButton(
    isExpanded: Boolean,
    onChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        IconButton(onClick = { onChanged(!isExpanded) }) {
            Icon(
                imageVector = if (isExpanded) {
                    Icons.Outlined.CloseFullscreen
                } else {
                    Icons.Outlined.OpenInFull
                },
                // TODO: description
                contentDescription = null
            )
        }
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