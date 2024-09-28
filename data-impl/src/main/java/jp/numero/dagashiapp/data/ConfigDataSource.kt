package jp.numero.dagashiapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val preferenceName = "config"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = preferenceName)

@Singleton
class ConfigDataSource(
    private val dataStore: DataStore<Preferences>
) {

    @Inject
    constructor(
        @ApplicationContext context: Context
    ) : this(context.dataStore)

    private val themeKey = stringPreferencesKey(themeKeyName)
    private val applyDynamicColorKey = booleanPreferencesKey(applyDynamicColorKeyName)

    val theme: Flow<Theme> = dataStore.data.map {
        val themeName = it[themeKey] ?: Theme.default.name
        checkNotNull(Theme.toList().find { theme -> theme.name == themeName })
    }
    val applyDynamicColor: Flow<Boolean> = dataStore.data.map { it[applyDynamicColorKey] ?: true }

    suspend fun updateTheme(theme: Theme) {
        dataStore.edit {
            it[themeKey] = theme.name
        }
    }

    suspend fun updateApplyDynamicColor(applyDynamicColor: Boolean) {
        dataStore.edit {
            it[applyDynamicColorKey] = applyDynamicColor
        }
    }

    companion object {
        private const val themeKeyName = "theme"
        private const val applyDynamicColorKeyName = "applyDynamicColor"
    }
}