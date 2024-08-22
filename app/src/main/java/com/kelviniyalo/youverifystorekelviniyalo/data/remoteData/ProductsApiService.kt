package com.kelviniyalo.youverifystorekelviniyalo.data.remoteData

import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductsApiService {

    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>
}