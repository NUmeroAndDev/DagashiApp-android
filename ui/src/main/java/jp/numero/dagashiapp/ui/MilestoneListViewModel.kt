package jp.numero.dagashiapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.numero.dagashiapp.model.MilestoneList
import jp.numero.dagashiapp.repository.DagashiRepository
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
        _uiState.value = uiState.value.copy(
            isInitialLoading = uiState.value.isEmpty,
            isRefreshing = isRefresh,
            isMoreLoading = nextCursor != null
        )
        viewModelScope.launch {
            val result = runCatching {
                if (nextCursor != null) {
                    dagashiRepository.fetchMoreMilestoneList(nextCursor)
                } else {
                    dagashiRepository.fetchMilestoneList()
                }
            }
            _uiState.value = uiState.value.copy(
                isInitialLoading = false,
                isRefreshing = false,
                isMoreLoading = false,
                data = result.getOrNull(),
                error = result.exceptionOrNull()
            )
        }
    }
}