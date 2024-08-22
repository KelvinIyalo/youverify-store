package com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.kelviniyalo.youverifystorekelviniyalo.domain.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderModel @Inject constructor(private val databaseRepository: DatabaseRepository) :
    ViewModel() {

    fun clearCart() {
        viewModelScope.launch {
            databaseRepository.clearCart()
        }
    }

    fun totalAmountInCart(): LiveData<Float?> {
        return databaseRepository.totalAmountInCart().map { it ?:0f }
    }
}