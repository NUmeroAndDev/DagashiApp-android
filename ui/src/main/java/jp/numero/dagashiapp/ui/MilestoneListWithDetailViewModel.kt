package jp.numero.dagashiapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.numero.dagashiapp.model.Milestone
import jp.numero.dagashiapp.model.MilestoneDetail
import jp.numero.dagashiapp.model.MilestoneList
import jp.numero.dagashiapp.repository.DagashiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilestoneListWithDetailViewModel @Inject constructor(
    private val dagashiRepository: DagashiRepository
) : ViewModel() {

    private val _uiStates: MutableStateFlow<MilestoneListWithDetailUiStates> = MutableStateFlow(
        MilestoneListWithDetailUiStates(UiState(), UiState())
    )
    val uiStates = _uiStates.asStateFlow()

    init {
        loadList(
            withLoadDetail = true
        )
    }

    fun loadListMore() {
        val milestoneList = uiStates.value.milestoneList.data ?: return
        if (milestoneList.hasMore) {
            loadList(nextCursor = milestoneList.nextCursor)
        }
    }

    fun selectedMilestone(milestone: Milestone) {
        loadDetail(milestone.path)
    }

    private fun loadList(
        isRefresh: Boolean = false,
        nextCursor: String? = null,
        withLoadDetail: Boolean = false
    ) {
        val listUiState = uiStates.value.milestoneList
        if (listUiState.isLoading) return
        _uiStates.value = uiStates.value.copy(
            milestoneList = listUiState.startLoading(isRefresh)
        )
        viewModelScope.launch {
            runCatching {
                val list = if (nextCursor != null) {
                    dagashiRepository.fetchMoreMilestoneList(nextCursor)
                } else {
                    dagashiRepository.fetchMilestoneList()
                }
                val firstMilestone = list.value.firstOrNull()
                val detail = if (withLoadDetail && firstMilestone != null) {
                    dagashiRepository.fetchMilestoneDetail(firstMilestone.path)
                } else {
                    uiStates.value.milestoneDetail.data
                }
                list to detail
            }.fold(
                onSuccess = { (list, detail) ->
                    _uiStates.value = uiStates.value.copy(
                        milestoneList = uiStates.value.milestoneList.handleData(list),
                        milestoneDetail = uiStates.value.milestoneDetail.handleData(detail),
                    )
                },
                onFailure = {
                    _uiStates.value = uiStates.value.copy(
                        milestoneList = uiStates.value.milestoneList.handleError(it),
                        milestoneDetail = uiStates.value.milestoneDetail.handleError(it),
                    )
                }
            )
        }
    }

    private fun loadDetail(path: String) {
        val detailUiState = uiStates.value.milestoneDetail
        if (detailUiState.isLoading) return
        _uiStates.value = uiStates.value.copy(
            milestoneDetail = detailUiState.copy(
                _isLoading = true,
                data = null
            )
        )
        viewModelScope.launch {
            runCatching {
                dagashiRepository.fetchMilestoneDetail(path)
            }.fold(
                onSuccess = {
                    _uiStates.value = uiStates.value.copy(
                        milestoneDetail = uiStates.value.milestoneDetail.handleData(it)
                    )
                },
                onFailure = {
                    _uiStates.value = uiStates.value.copy(
                        milestoneDetail = uiStates.value.milestoneDetail.handleError(it)
                    )
                }
            )
        }
    }
}

data class MilestoneListWithDetailUiStates(
    val milestoneList: UiState<MilestoneList>,
    val milestoneDetail: UiState<MilestoneDetail>
)