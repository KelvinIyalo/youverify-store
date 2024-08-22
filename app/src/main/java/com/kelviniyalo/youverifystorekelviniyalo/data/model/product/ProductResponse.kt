package com.kelviniyalo.youverifystorekelviniyalo.data.model.product

data class ProductResponse(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)