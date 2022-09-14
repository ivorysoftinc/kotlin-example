package com.ivorysoftinc.kotlin.example.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ivorysoftinc.kotlin.example.data.BASE_URL
import com.ivorysoftinc.kotlin.example.data.Card
import com.ivorysoftinc.kotlin.example.network.CardDeserializer
import com.ivorysoftinc.kotlin.example.network.CardsApi
import com.ivorysoftinc.kotlin.example.resources.strings.SOMETHING_WENT_WRONG
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.Collections
import java.util.concurrent.Executors

/**
 * Koin module that provides all Network dependencies.
 */
val networkModule = module {
    single { provideInterceptors() }
    single { provideOkHttpClient(get(), get(), get()) }
    single { provideBaseUrl() }
    single { provideRetrofit(get(), get(), get()) }
    single { provideDataApi(get()) }
    single { provideGson() }
    single { provideConverterFactory(get()) }
    single { provideConnectionSpec() }
}

fun provideInterceptors(): List<Interceptor> =
    listOf(ErrorInterceptor(), provideLoggingInterceptor())

fun provideOkHttpClient(
    interceptors: List<Interceptor>,
    networkInterceptors: List<Interceptor>,
    connectionSpec: ConnectionSpec
): OkHttpClient = OkHttpClient().newBuilder()
    .apply {
        connectionSpecs(Collections.singletonList(connectionSpec))
        interceptors.forEach { addInterceptor(it) }
        networkInterceptors.forEach { addNetworkInterceptor(it) }
    }
    .build()

fun provideBaseUrl(): String = BASE_URL

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory,
    baseUrl: String
): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(converterFactory)
    .also { it.callbackExecutor(Executors.newSingleThreadExecutor()) }
    .build()

fun provideDataApi(retrofit: Retrofit): CardsApi = retrofit.create(CardsApi::class.java)

fun provideGson(): Gson = GsonBuilder()
    .registerTypeAdapter(Card::class.java, CardDeserializer())
    .create()

fun provideConverterFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)

fun provideConnectionSpec(): ConnectionSpec =
    ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .allEnabledTlsVersions()
        .allEnabledCipherSuites()
        .build()

fun provideLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            throw NetworkException()
        }

        if (response.isSuccessful.not()) throw NetworkException()
        return response
    }
}

class NetworkException(override val message: String = SOMETHING_WENT_WRONG) : IOException(message)
