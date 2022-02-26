package jp.numero.dagashiapp.appwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.*
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
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
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(R.color.appwidget_background)
                .appWidgetBackground()
                .padding(16.dp),
        ) {
            Text(
                text = "DagashiApp",
                style = TextStyle(
                    fontWeight = FontWeight.Medium
                ),
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = GlanceModifier.height(12.dp))
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
                        modifier = GlanceModifier.fillMaxSize()
                    )
                }
            )
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
        items(
            items = milestoneList.value,
            itemId = {
                it.number.toLong()
            }
        ) { item ->
            MilestoneItem(
                milestone = item,
                modifier = GlanceModifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MilestoneItem(
    milestone: Milestone,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = "#${milestone.number}",
        )
        Spacer(modifier = GlanceModifier.height(4.dp))
        Text(
            text = milestone.description,
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