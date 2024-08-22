package com.kelviniyalo.youverifystorekelviniyalo.data.local_data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: Product): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: Product)

    @Query("DELETE FROM ProductTable")
    suspend fun deleteAll()

    @Query("DELETE FROM ProductTable WHERE id = :id")
    suspend fun deleteProductById(id: Int)

    @Query("""UPDATE ProductTable SET isInCart = 0 WHERE isInCart = 1""")
    suspend fun clearCart()


    @Query("""SELECT SUM(itemAmount)  FROM ProductTable WHERE  isInCart = 1""")
    fun totalAmountInCart(): LiveData<Float?>

    @Query("""SELECT * FROM ProductTable ORDER BY id ASC""")
    fun getAllProduct(): LiveData<List<Product>>

    @Query("""SELECT * FROM ProductTable WHERE  isInCart = 1""")
    fun getAllProductInCart(): LiveData<List<Product>>
}