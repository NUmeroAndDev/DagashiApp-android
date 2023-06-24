package jp.numero.dagashiapp.feature.milestones.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.numero.dagashiapp.model.MilestoneDetail
import jp.numero.dagashiapp.repository.DagashiRepository
import jp.numero.dagashiapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MilestoneDetailViewModel @Inject constructor(
    private val dagashiRepository: DagashiRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<MilestoneDetail>> = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val path = savedStateHandle.getStateFlow<String?>(pathKey, null)

    init {
        // TODO: Implement refresh
        path
            .filterNotNull()
            .onEach {
                _uiState.value = uiState.value.startLoading(false)
                runCatching {
                    dagashiRepository.fetchMilestoneDetail(it)
                }.fold(
                    onSuccess = {
                        _uiState.value = uiState.value.handleData(it)
                    },
                    onFailure = {
                        _uiState.value = uiState.value.handleError(it)
                    }
                )
            }
            .catch {
                _uiState.value = uiState.value.handleError(it)
            }
            .launchIn(viewModelScope)
    }

    fun load(path: String) {
        savedStateHandle[pathKey] = path
    }

    companion object {
        private const val pathKey = "path"
    }
}