package com.kelviniyalo.youverifystorekelviniyalo.common.handler

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody

fun handleApiHttpException(e: ResponseBody): ApiErrorResponse? {
    return try {
        e.source().let {
            val moshiAdapter = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
                .adapter(ApiErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (t: Throwable) {
        ApiErrorResponse("An unexpected error occurred")
    }
}