package jp.numero.dagashiapp.appwidget

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val widgetStateHolder: WidgetStateHolder,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return runCatching {
            widgetStateHolder.invalidate()
            LatestMilestoneWidget().updateAll(appContext)
        }.fold(
            onSuccess = { Result.success() },
            onFailure = { Result.failure() }
        )
    }

    companion object {
        fun setupWorkerRequest() = PeriodicWorkRequestBuilder<DataSyncWorker>(1, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        internal const val WorkerName = "DataSyncWorker"
    }
}