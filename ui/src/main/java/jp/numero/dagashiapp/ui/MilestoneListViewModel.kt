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
        // TODO: impl load more
    }

    private fun load(isRefresh: Boolean = false) {
        _uiState.value = uiState.value.copy(isLoading = true, isRefreshing = isRefresh)
        viewModelScope.launch {
            val result = runCatching {
                dagashiRepository.fetchMilestoneList()
            }
            _uiState.value = uiState.value.copy(
                isLoading = false,
                isRefreshing = false,
                data = result.getOrNull(),
                error = result.exceptionOrNull()
            )
        }
    }
}