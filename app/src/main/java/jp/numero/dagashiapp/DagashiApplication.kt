package jp.numero.dagashiapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import jp.numero.dagashiapp.feature.settings.AppVersion
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidApp
class DagashiApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}

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
