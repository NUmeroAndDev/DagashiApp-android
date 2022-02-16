package jp.numero.dagashiapp.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import jp.numero.dagashiapp.model.Issue
import jp.numero.dagashiapp.model.Label
import jp.numero.dagashiapp.model.MilestoneDetail
import jp.numero.dagashiapp.ui.component.FullScreenLoadingIndicator
import jp.numero.dagashiapp.ui.component.LinkedText
import jp.numero.dagashiapp.ui.component.TopAppBar
import jp.takuji31.compose.navigation.screen.ScreenNavController

@Composable
fun MilestoneDetailScreen(navController: ScreenNavController) {
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
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneDetailScreen(
    uiState: UiState<MilestoneDetail>,
    onBack: () -> Unit,
    onClickShare: (String) -> Unit,
) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
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
                contentPadding = rememberInsetsPaddingValues(
                    LocalWindowInsets.current.statusBars,
                    applyBottom = false,
                ),
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
                    loadSucceed = {
                        MilestoneDetailContent(milestoneDetail = it)
                    },
                    loadFailed = {
                        // TODO: impl show error message
                    }
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneDetailContent(
    milestoneDetail: MilestoneDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        OutlinedCard(
            modifier = Modifier.padding(
                rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.systemBars,
                    applyTop = false,
                    additionalStart = 16.dp,
                    additionalEnd = 16.dp,
                    additionalBottom = 16.dp,
                    additionalTop = 16.dp
                )
            )
        ) {
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

        LinkedText(
            text = issue.body,
            style = MaterialTheme.typography.bodyMedium
        )
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