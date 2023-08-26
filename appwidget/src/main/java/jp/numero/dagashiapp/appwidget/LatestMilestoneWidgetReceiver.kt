package jp.numero.dagashiapp.appwidget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LatestMilestoneWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var stateHolder: WidgetStateHolder

    override val glanceAppWidget: GlanceAppWidget = LatestMilestoneWidget()

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        CoroutineScope(Dispatchers.IO).launch {
            stateHolder.invalidate()
        }
    }
}