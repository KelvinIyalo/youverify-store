package com.kelviniyalo.youverifystorekelviniyalo.data.repository

import com.kelviniyalo.youverifystorekelviniyalo.common.handler.RepositoryResponse
import com.kelviniyalo.youverifystorekelviniyalo.common.handler.exceptionHandler
import com.kelviniyalo.youverifystorekelviniyalo.common.handler.getCallResponseState
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.ProductResponse
import com.kelviniyalo.youverifystorekelviniyalo.data.remoteData.ProductsApiService
import com.kelviniyalo.youverifystorekelviniyalo.domain.repository.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val apiService: ProductsApiService) :
    ProductsRepository {
    override suspend fun getProducts(): RepositoryResponse<ProductResponse> {
        return try {
            val result = apiService.getProducts()
            getCallResponseState(result)
        } catch (exception: Exception) {
            return exceptionHandler(exception)
        }
    }
}