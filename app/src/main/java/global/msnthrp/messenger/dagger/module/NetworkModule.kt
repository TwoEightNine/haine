package global.msnthrp.messenger.dagger.module

import dagger.Module
import dagger.Provides
import global.msnthrp.messenger.App
import global.msnthrp.messenger.BuildConfig
import global.msnthrp.messenger.db.DbHelper
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.storage.Session
import global.msnthrp.messenger.utils.ApiUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by msnthrp on 22/01/18.
 */

@Module
class NetworkModule {

    private val timeout = 30L

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val log = HttpLoggingInterceptor()
        log.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return log
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(session: Session): AuthInterceptor = AuthInterceptor(session)

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                            authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideNetwork(client: OkHttpClient): Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(App.BASE_URL)
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiUtils(apiService: ApiService, dbHelper: DbHelper): ApiUtils = ApiUtils(apiService, dbHelper)

    inner class AuthInterceptor(private val session: Session) : Interceptor {

        private val authToken = "Auth-Token"

        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val url = request.url()
                    .newBuilder()
                    .build()

            request = request.newBuilder()
                    .url(url)
                    .addHeader(authToken, session.token)
                    .build()
            return chain.proceed(request)
        }
    }
}