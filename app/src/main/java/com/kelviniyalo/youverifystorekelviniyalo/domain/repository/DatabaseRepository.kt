package com.kelviniyalo.youverifystorekelviniyalo.domain.repository

import androidx.lifecycle.LiveData
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product

interface DatabaseRepository {

    suspend fun saveToDb(entity: Product): Long

    suspend fun update(entity: Product)

    fun getAllProduct(): LiveData<List<Product>>

    fun getAllProductInCart(): LiveData<List<Product>>

    suspend fun deleteAllFromDb()

    fun totalAmountInCart(): LiveData<Float?>

    suspend fun deleteProductById(id: Int)

    suspend fun clearCart()
}