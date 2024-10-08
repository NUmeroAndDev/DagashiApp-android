package jp.numero.dagashiapp.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.numero.dagashiapp.data.ConfigRepository
import jp.numero.dagashiapp.data.Config
import jp.numero.dagashiapp.data.Theme
import jp.numero.dagashiapp.ui.lifecycleStateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val appVersion: AppVersion,
    private val configRepository: ConfigRepository,
) : ViewModel() {

    val config = configRepository.observe()
        .lifecycleStateIn(Config())

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            configRepository.updateTheme(theme)
        }
    }

    fun updateApplyDynamicColor(applyDynamicColor: Boolean) {
        viewModelScope.launch {
            configRepository.updateApplyDynamicColor(applyDynamicColor)
        }
    }
}