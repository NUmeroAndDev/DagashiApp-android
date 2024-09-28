package jp.numero.dagashiapp.appwidget

import jp.numero.dagashiapp.data.DagashiRepository
import jp.numero.dagashiapp.data.Milestone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetStateHolder @Inject constructor(
    private val repository: DagashiRepository,
) {

    private val _latestMilestone =
        MutableStateFlow<WidgetState<Milestone>>(WidgetState(isLoading = true))
    val latestMilestone = _latestMilestone.asStateFlow()

    suspend fun invalidate() {
        val state = runCatching {
            val milestones = repository.fetchMilestoneList()
            milestones.value.firstOrNull()
        }.fold(
            onSuccess = {
                WidgetState(data = it, updatedAt = Instant.now())
            },
            onFailure = {
                WidgetState(error = it, updatedAt = Instant.now())
            }
        )
        _latestMilestone.value = state
    }
}

data class WidgetState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: Throwable? = null,
    val updatedAt: Instant = Instant.now(),
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