package jp.numero.dagashiapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import jp.numero.dagashiapp.model.Issue
import jp.numero.dagashiapp.model.MilestoneDetail
import jp.numero.dagashiapp.ui.component.FullScreenLoadingIndicator
import jp.numero.dagashiapp.ui.component.LinkedText
import jp.numero.dagashiapp.ui.component.TopAppBar
import jp.takuji31.compose.navigation.screen.ScreenNavController

@Composable
fun MilestoneDetailScreen(navController: ScreenNavController) {
    val viewModel: MilestoneDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    MilestoneDetailScreen(
        uiState = uiState,
        onBack = {
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneDetailScreen(
    uiState: UiState<MilestoneDetail>,
    onBack: () -> Unit
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
                onBack = onBack
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

@Composable
fun MilestoneDetailContent(
    milestoneDetail: MilestoneDetail,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = false,
        )
    ) {
        itemsIndexed(
            items = milestoneDetail.issues,
            key = { _, item ->
                item.title
            }
        ) { index, item ->
            IssueItem(issue = item)
            if (index != milestoneDetail.issues.lastIndex) {
                Divider()
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
        // TODO: impl tag
        Spacer(modifier = Modifier.height(8.dp))

        LinkedText(
            text = issue.body,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}