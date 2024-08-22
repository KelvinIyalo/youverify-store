package com.kelviniyalo.youverifystorekelviniyalo.domain.repository

import com.kelviniyalo.youverifystorekelviniyalo.common.handler.RepositoryResponse
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.ProductResponse

interface ProductsRepository {
    suspend fun getProducts(): RepositoryResponse<ProductResponse>
}