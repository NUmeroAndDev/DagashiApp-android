package jp.numero.dagashiapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.numero.dagashiapp.model.Milestone
import jp.numero.dagashiapp.model.MilestoneList

@Composable
fun MilestoneListScreen() {
    // TODO: Impl viewModel
    MilestoneListScreen(
        milestoneList = MilestoneList(
            (0..21).map {
                Milestone(
                    id = it.toString(),
                    number = 100 + it,
                    description = "Description",
                    path = "",
                    closedAd = "2020-09-13"
                )
            }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneListScreen(
    milestoneList: MilestoneList
) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = {
            MilestoneListContent(
                milestoneList = milestoneList,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        },
    )
}

@Composable
fun MilestoneListContent(
    milestoneList: MilestoneList,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = milestoneList.value,
            key = {
                it.id
            }
        ) {
            MilestoneItem(
                milestone = it,
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun MilestoneItem(
    milestone: Milestone,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = { onClick() })
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${milestone.number}",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = milestone.closedAd,
                style = MaterialTheme.typography.labelSmall,
                color = LocalContentColor.current.copy(alpha = 0.54f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = milestone.description,
            style = MaterialTheme.typography.bodyMedium,
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
            closedAd = "2020-09-13"
        ),
        onClick = {}
    )
}