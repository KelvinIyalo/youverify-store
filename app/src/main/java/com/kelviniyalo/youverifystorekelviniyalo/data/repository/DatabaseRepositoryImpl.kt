package com.kelviniyalo.youverifystorekelviniyalo.data.repository

import androidx.lifecycle.LiveData
import com.kelviniyalo.youverifystorekelviniyalo.data.local_data.DatabaseDao
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.domain.repository.DatabaseRepository
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(private val databaseDao: DatabaseDao): DatabaseRepository {

    override suspend fun saveToDb(entity: Product): Long {
        return databaseDao.insert(entity)
    }

    override suspend fun update(entity: Product) {
        return databaseDao.update(entity)
    }

    override fun getAllProduct(): LiveData<List<Product>> {
       return databaseDao.getAllProduct()
    }

    override fun getAllProductInCart(): LiveData<List<Product>> {
       return databaseDao.getAllProductInCart()
    }

    override suspend fun deleteAllFromDb() {
      return databaseDao.deleteAll()
    }

    override fun totalAmountInCart(): LiveData<Float?> {
        return databaseDao.totalAmountInCart()
    }

    override suspend fun deleteProductById(id: Int) {
        return databaseDao.deleteProductById(id)
    }

    override suspend fun clearCart() {
        databaseDao.clearCart()
    }
}