package jp.numero.dagashiapp.feature.milestones

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MilestonesContainerViewModel @Inject constructor() : ViewModel() {

    private val _selectedPath = MutableStateFlow<String?>(null)
    val selectedPath = _selectedPath.asStateFlow()

    private val _expanded = MutableStateFlow(false)
    val expanded = _expanded.asStateFlow()

    fun openDetail(path: String) {
        _selectedPath.value = path
    }

    fun closeDetail() {
        _selectedPath.value = null
    }

    fun expandDetail() {
        _expanded.value = true
    }

    fun collapseDetail() {
        _expanded.value = false
    }
}