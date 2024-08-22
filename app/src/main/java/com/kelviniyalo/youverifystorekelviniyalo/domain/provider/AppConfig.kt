package com.kelviniyalo.youverifystorekelviniyalo.domain.provider

import okhttp3.OkHttpClient

interface AppConfig {

    /**
     * Okhttp client should be shared across the app for performance reasons. Other modules can have access to the okhttp client.
     * They can however customize it by creating newBuilder out of the shared client. Like so
     *
     * kotlin
     *  okHttpClient.newBuilder()
     *      .readTimeout(500, TimeUnit.MILLISECONDS)
     *      .build()
     *
     */
    val okHttpClient: OkHttpClient

}