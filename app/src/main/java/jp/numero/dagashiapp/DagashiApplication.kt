package jp.numero.dagashiapp

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import jp.numero.dagashiapp.feature.settings.AppVersion
import javax.inject.Singleton

@HiltAndroidApp
class DagashiApplication : Application()

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideAppVersion(): AppVersion {
        return AppVersion(
            code = BuildConfig.VERSION_CODE,
            name = BuildConfig.VERSION_NAME
        )
    }
}
