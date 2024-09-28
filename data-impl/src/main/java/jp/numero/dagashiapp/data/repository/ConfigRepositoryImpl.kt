package jp.numero.dagashiapp.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.numero.dagashiapp.data.ConfigDataSource
import jp.numero.dagashiapp.data.ConfigRepository
import jp.numero.dagashiapp.data.Config
import jp.numero.dagashiapp.data.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

internal class ConfigRepositoryImpl @Inject constructor(
    private val configDataSource: ConfigDataSource,
) : ConfigRepository {

    override fun observe(): Flow<Config> = combine(
        configDataSource.theme,
        configDataSource.applyDynamicColor
    ) { theme, useDynamicColor ->
        Config(theme, useDynamicColor)
    }

    override suspend fun updateTheme(theme: Theme) {
        configDataSource.updateTheme(theme)
    }

    override suspend fun updateApplyDynamicColor(applyDynamicColor: Boolean) {
        configDataSource.updateApplyDynamicColor(applyDynamicColor)
    }

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class RepositoryModule {
        @Singleton
        @Binds
        abstract fun bindConfigRepository(impl: ConfigRepositoryImpl): ConfigRepository
    }
}