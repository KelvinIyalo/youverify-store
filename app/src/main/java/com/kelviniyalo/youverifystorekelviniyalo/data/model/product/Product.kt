package com.kelviniyalo.youverifystorekelviniyalo.data.model.product

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ProductTable")
data class Product(
    val availabilityStatus: String? = "",
    val brand: String? = "",
    val category: String? = "",
    val description: String? = "",
    val discountPercentage: Double? = 0.0,
    @PrimaryKey(autoGenerate = false)
    val id: Int? = 0,
    val price: Double? = 0.0,
    val rating: Double? = 0.0,
    val itemAmount: Double? = 0.0,
    val stock: Int? = 0,
    val thumbnail: String? = "",
    val shippingInformation: String? = "",
    val title: String? = "",
    val cat: String? = "",
    val isInCart: Boolean = false,
    val itemCount: Int? = 0,
    val isHeader: Boolean = false,
    var productColor: Int = 0,
    var imageByteArray: ByteArray? = null
) : Serializable