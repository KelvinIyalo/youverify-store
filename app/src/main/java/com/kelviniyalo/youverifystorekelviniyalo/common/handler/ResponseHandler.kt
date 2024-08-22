package com.kelviniyalo.youverifystorekelviniyalo.common.handler

import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

 fun <T> getCallResponseState(result: Response<T>): RepositoryResponse<T> {

    return when {
        result.isSuccessful && result.body() != null -> {
            RepositoryResponse.Success(result.body() as T)
        }

        (result.code() == HttpURLConnection.HTTP_FORBIDDEN || result.code() == HttpURLConnection.HTTP_UNAUTHORIZED) -> {
            RepositoryResponse.ApiError(result.message())
        }
        else -> {
            val apiError = result.errorBody()?.let { handleApiHttpException(it) }
            RepositoryResponse.Error(apiError?.message.toString())
        }

    }
}

 fun <T> exceptionHandler(exception: Exception): RepositoryResponse<T> {
    return when (exception) {
        is IOException -> {
            RepositoryResponse.Error(NETWORK_EXCEPTION)
        }
        else -> {
            RepositoryResponse.Error(exception.message.toString())
        }
    }
}

const val NETWORK_EXCEPTION = "No Internet Connection"