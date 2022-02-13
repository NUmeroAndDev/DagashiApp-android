package jp.numero.dagashiapp.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.numero.dagashiapp.model.MilestoneDetail
import jp.numero.dagashiapp.repository.DagashiRepository
import jp.takuji31.compose.navigation.screen.screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilestoneDetailViewModel @Inject constructor(
    private val dagashiRepository: DagashiRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<MilestoneDetail>> = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val screen: Screen.MilestoneDetail by savedStateHandle.screen()

    init {
        load()
    }

    fun refresh() {
        load(isRefresh = true)
    }

    private fun load(
        isRefresh: Boolean = false,
    ) {
        if (uiState.value.isLoading) return
        _uiState.value = uiState.value.copy(
            isInitialLoading = uiState.value.isEmpty,
            isRefreshing = isRefresh,
        )
        viewModelScope.launch {
            val result = runCatching {
                dagashiRepository.fetchMilestoneDetail(screen.path)
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