package jp.numero.dagashiapp.feature.milestones.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.numero.dagashiapp.data.DagashiRepository
import jp.numero.dagashiapp.data.MilestoneList
import jp.numero.dagashiapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilestoneListViewModel @Inject constructor(
    private val dagashiRepository: DagashiRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<MilestoneList>> = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun refresh() {
        load(isRefresh = true)
    }

    fun loadMore() {
        val data = uiState.value.data ?: return
        if (data.hasMore) {
            load(nextCursor = data.nextCursor)
        }
    }

    private fun load(
        isRefresh: Boolean = false,
        nextCursor: String? = null
    ) {
        if (uiState.value.isLoading) return
        _uiState.value = uiState.value.startLoading(isRefresh)
        viewModelScope.launch {
            runCatching {
                if (nextCursor != null) {
                    dagashiRepository.fetchMoreMilestoneList(nextCursor)
                } else {
                    dagashiRepository.fetchMilestoneList()
                }
            }.fold(
                onSuccess = {
                    _uiState.value = uiState.value.endLoading(data = it)
                },
                onFailure = {
                    _uiState.value = uiState.value.endLoading(error = it)
                }
            )
        }
    }
}