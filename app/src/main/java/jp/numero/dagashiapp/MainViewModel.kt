package jp.numero.dagashiapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.numero.dagashiapp.data.ConfigRepository
import jp.numero.dagashiapp.ui.lifecycleStateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
) : ViewModel() {

    private val initialConfig
        get() = runBlocking {
            configRepository.observe().first()
        }

    val config = configRepository.observe()
        .lifecycleStateIn(initialConfig)
}