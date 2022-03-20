package jp.numero.dagashiapp.appwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import dagger.hilt.android.AndroidEntryPoint
import jp.numero.dagashiapp.model.Milestone
import jp.numero.dagashiapp.model.MilestoneList
import jp.numero.dagashiapp.repository.DagashiRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MilestoneListWidget : GlanceAppWidget() {

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    private var state: WidgetState<MilestoneList> = WidgetState()

    @Composable
    override fun Content() {
        WidgetTheme {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(WidgetTheme.colorScheme.background)
                    .appWidgetBackgroundRadius()
                    .appWidgetBackground(),
            ) {
                Text(
                    text = "DagashiApp",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(WidgetTheme.colorScheme.textPrimary),
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
                                .padding(horizontal = 16.dp)
                        )
                    }
                )
            }
        }
    }

    suspend fun updateState(
        widgetState: WidgetState<MilestoneList>,
        context: Context,
        glanceId: GlanceId
    ) {
        state = widgetState
        update(context, glanceId)
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
                        .background(WidgetTheme.colorScheme.innerBackground)
                        .appWidgetInnerRadius()
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
                color = ColorProvider(WidgetTheme.colorScheme.textPrimary),
            ),
        )
        Spacer(modifier = GlanceModifier.height(4.dp))
        Text(
            text = milestone.description,
            style = TextStyle(
                fontSize = 12.sp,
                color = ColorProvider(WidgetTheme.colorScheme.textSecondary),
            ),
            maxLines = 2
        )
    }
}

@AndroidEntryPoint
class MilestoneListWidgetReceiver : GlanceAppWidgetReceiver() {

    private val coroutineScope = MainScope()

    @Inject
    lateinit var repository: DagashiRepository

    private val milestoneListWidget = MilestoneListWidget()

    override val glanceAppWidget: GlanceAppWidget = milestoneListWidget

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateData(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        updateData(context)
    }

    private fun updateData(context: Context) {
        coroutineScope.launch {
            val glanceId = GlanceAppWidgetManager(context)
                .getGlanceIds(MilestoneListWidget::class.java)
                .firstOrNull() ?: return@launch

            milestoneListWidget.updateState(
                widgetState = WidgetState(isLoading = true),
                context = context,
                glanceId = glanceId
            )

            val state = runCatching {
                repository.fetchMilestoneList()
            }.fold(
                onSuccess = {
                    WidgetState(data = it)
                },
                onFailure = {
                    WidgetState(error = it)
                }
            )
            milestoneListWidget.updateState(
                widgetState = state,
                context = context,
                glanceId = glanceId
            )
        }
    }
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