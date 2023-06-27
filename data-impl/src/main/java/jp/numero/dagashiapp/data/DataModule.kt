package jp.numero.dagashiapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    @ElementsIntoSet
    fun provideEmptyInterceptors(): Set<Interceptor> = emptySet()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply { interceptors.forEach(::addInterceptor) }
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideDagashiApi(
        okHttpClient: OkHttpClient
    ): DagashiApi {
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://androiddagashi.github.io")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        return retrofit.create(DagashiApi::class.java)
    }
}