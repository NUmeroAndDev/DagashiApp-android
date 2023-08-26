package jp.numero.dagashiapp.appwidget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LatestMilestoneWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var stateHolder: WidgetStateHolder

    override val glanceAppWidget: GlanceAppWidget = LatestMilestoneWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DataSyncWorker.WorkerName,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            DataSyncWorker.setupWorkerRequest(),
        )
    }
}