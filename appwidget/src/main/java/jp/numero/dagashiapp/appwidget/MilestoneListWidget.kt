package jp.numero.dagashiapp.appwidget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import jp.numero.dagashiapp.data.DagashiRepository
import jp.numero.dagashiapp.model.Milestone
import jp.numero.dagashiapp.model.MilestoneList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MilestoneListWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPoints.get(context, AppWidgetModule::class.java)
        provideContent {
            var state by remember { mutableStateOf(WidgetState<MilestoneList>()) }
            LaunchedEffect(Unit) {
                state = runCatching {
                    entryPoint.provideRepository().fetchMilestoneList()
                }.fold(
                    onSuccess = {
                        WidgetState(data = it)
                    },
                    onFailure = {
                        WidgetState(error = it)
                    }
                )
            }
            DagashiWidget(state = state)
        }
    }

    @Composable
    private fun DagashiWidget(
        state: WidgetState<MilestoneList>
    ) {
        GlanceTheme {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .appWidgetBackground()
                    .background(GlanceTheme.colors.background)
                    .appWidgetBackgroundRadius()
            ) {
                Text(
                    text = "DagashiApp",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = GlanceTheme.colors.onBackground,
                    ),
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
                state.onState(
                    loading = {
                        Box(
                            modifier = GlanceModifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    loadFailed = {
                        // TODO: error
                    },
                    loadSucceed = {
                        MilestoneListContent(
                            milestoneList = it,
                            modifier = GlanceModifier
                                .fillMaxSize()
                                .background(GlanceTheme.colors.surfaceVariant)
                                .appWidgetInnerRadius()
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun MilestoneListContent(
    milestoneList: MilestoneList,
    modifier: GlanceModifier = GlanceModifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(
            items = milestoneList.value,
            itemId = { _, item ->
                item.number.toLong()
            }
        ) { index, item ->
            Column {
                MilestoneItem(
                    milestone = item,
                    modifier = GlanceModifier
                        .clickable(
                            actionStartActivity(Intent().apply {
                                setClassName(
                                    "jp.numero.dagashiapp",
                                    "jp.numero.dagashiapp.MainActivity"
                                )
                            })
                        )
                        .fillMaxWidth()
                )
                if (index != milestoneList.value.lastIndex) {
                    Spacer(modifier = GlanceModifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun MilestoneItem(
    milestone: Milestone,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "#${milestone.number}",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = GlanceTheme.colors.onBackground,
            ),
        )
        Spacer(modifier = GlanceModifier.height(4.dp))
        Text(
            text = milestone.description,
            style = TextStyle(
                fontSize = 12.sp,
                color = GlanceTheme.colors.onBackground,
            ),
            maxLines = 2
        )
    }
}

@AndroidEntryPoint
class MilestoneListWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var repository: DagashiRepository

    private val milestoneListWidget = MilestoneListWidget()

    override val glanceAppWidget: GlanceAppWidget = milestoneListWidget

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        CoroutineScope(Dispatchers.IO).launch {
            // FIXME: Update latest issue
            repository.fetchMilestoneList()
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppWidgetModule {
    fun provideRepository(): DagashiRepository
}

data class WidgetState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: Throwable? = null,
) {
    inline fun onState(
        loading: () -> Unit = {},
        loadFailed: (error: Throwable) -> Unit = {},
        loadSucceed: (data: T) -> Unit = {},
    ) {
        if (isLoading) {
            loading()
        } else if (error != null) {
            loadFailed(error)
        } else if (data != null) {
            loadSucceed(data)
        }
    }
}