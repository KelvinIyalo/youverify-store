package com.kelviniyalo.youverifystorekelviniyalo.common.handler

data class ApiErrorResponse(
    val message: String? = null,
    val validationMessages: List<String>? = null,
    val errorCode: Int? = null
)