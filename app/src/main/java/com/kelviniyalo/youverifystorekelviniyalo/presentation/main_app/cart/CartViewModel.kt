package com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.domain.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) :
    ViewModel() {


    fun getAllProductInCart(): LiveData<List<Product>> {
        return databaseRepository.getAllProductInCart()
    }

    fun removeProductFromCart(id: Int) {
        viewModelScope.launch {
            databaseRepository.deleteProductById(id)
        }
    }

    fun totalAmountInCart(): LiveData<Float?> {
        return databaseRepository.totalAmountInCart().map { it ?:0f }
    }
}