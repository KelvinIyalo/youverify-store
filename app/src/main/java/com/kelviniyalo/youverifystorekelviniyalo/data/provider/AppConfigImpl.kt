package com.kelviniyalo.youverifystorekelviniyalo.data.provider

import com.kelviniyalo.youverifystorekelviniyalo.domain.provider.AppConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class AppConfigImpl : AppConfig {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    override val okHttpClient = OkHttpClient.Builder().apply {
        writeTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request = chain
                .request()
                .newBuilder()
                .build()
            chain.proceed(request)
        })
        addInterceptor(loggingInterceptor)
    }.build()
}