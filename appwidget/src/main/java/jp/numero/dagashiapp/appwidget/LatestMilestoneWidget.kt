package jp.numero.dagashiapp.appwidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
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
import dagger.hilt.components.SingletonComponent
import jp.numero.dagashiapp.data.Milestone
import jp.numero.dagashiapp.ui.dateTimeString

class LatestMilestoneWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            LatestMilestone()
        }
    }

    @Composable
    private fun LatestMilestone() {
        val context = LocalContext.current
        val entryPoint = EntryPoints.get(context, AppWidgetModule::class.java)
        val state by entryPoint.provideStateHolder().latestMilestone.collectAsState()
        GlanceTheme {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .appWidgetBackground()
                    .background(GlanceTheme.colors.background)
                    .appWidgetBackgroundRadius()
            ) {
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
                        Column(
                            modifier = GlanceModifier.fillMaxSize(),
                        ) {
                            Text(
                                text = "Error",
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = GlanceTheme.colors.onBackground,
                                ),
                            )
                            Spacer(GlanceModifier.height(8.dp))
                            Text(
                                text = it.message.orEmpty(),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = GlanceTheme.colors.error,
                                ),
                            )
                        }
                    },
                    loadSucceed = {
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Latest",
                                    style = TextStyle(
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = GlanceTheme.colors.onBackground,
                                    ),
                                )
                                Spacer(GlanceModifier.defaultWeight())
                                Text(
                                    text = "#${it.number}",
                                    style = TextStyle(
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = GlanceTheme.colors.onBackground,
                                    ),
                                )
                            }
                            Spacer(GlanceModifier.height(12.dp))
                            IssueListContent(
                                milestone = it,
                                modifier = GlanceModifier
                                    .defaultWeight()
                                    .background(GlanceTheme.colors.surfaceVariant)
                                    .appWidgetInnerRadius()
                            )
                            Spacer(GlanceModifier.height(8.dp))
                            Text(
                                text = LocalContext.current.getString(
                                    R.string.updated_at,
                                    dateTimeString(
                                        instant = state.updatedAt,
                                        format = LocalContext.current.getString(R.string.updated_at_format)
                                    )
                                ),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = GlanceTheme.colors.onSurfaceVariant,
                                ),
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun IssueListContent(
    milestone: Milestone,
    modifier: GlanceModifier = GlanceModifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Spacer(GlanceModifier.height(8.dp))
        }
        items(
            items = milestone.issues,
        ) { item ->
            Column(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "・${item.title}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = GlanceTheme.colors.onBackground,
                    ),
                )
            }
        }
        item {
            Spacer(GlanceModifier.height(8.dp))
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppWidgetModule {
    fun provideStateHolder(): WidgetStateHolder
}