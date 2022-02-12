package jp.numero.dagashiapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.numero.dagashiapp.model.Milestone
import jp.numero.dagashiapp.model.MilestoneList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilestoneListViewModel @Inject constructor(
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
            // TODO: impl load
            val result = runCatching {
                delay(1000L)
                MilestoneList(
                    (0..21).map {
                        Milestone(
                            id = it.toString(),
                            number = 100 + it,
                            description = "Description",
                            path = "",
                            closedAd = "2020-09-13"
                        )
                    }
                )
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