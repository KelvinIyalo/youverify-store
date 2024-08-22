package com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.product_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.domain.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) :
    ViewModel() {

    fun getAllProductInCart(): LiveData<List<Product>> {
        return databaseRepository.getAllProductInCart()
    }

    fun updateCart(entity: Product){
        viewModelScope.launch {
            databaseRepository.update(entity)
        }
    }


}