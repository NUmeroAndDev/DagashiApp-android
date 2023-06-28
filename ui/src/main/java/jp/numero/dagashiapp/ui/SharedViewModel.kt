package jp.numero.dagashiapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.numero.dagashiapp.data.ConfigRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
) : ViewModel() {

    private val initialConfig
        get() = runBlocking {
            configRepository.observe().first()
        }

    val config = configRepository.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialConfig)
}