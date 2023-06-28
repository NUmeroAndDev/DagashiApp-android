package jp.numero.dagashiapp.data

import jp.numero.dagashiapp.model.Config
import jp.numero.dagashiapp.model.Theme
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    fun observe(): Flow<Config>
    suspend fun updateTheme(theme: Theme)
    suspend fun updateApplyDynamicColor(applyDynamicColor: Boolean)
}