package com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelviniyalo.youverifystorekelviniyalo.common.handler.RepositoryResponse
import com.kelviniyalo.youverifystorekelviniyalo.common.handler.UiState
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.domain.repository.DatabaseRepository
import com.kelviniyalo.youverifystorekelviniyalo.domain.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val databaseRepository: DatabaseRepository
) :
    ViewModel() {


    fun getMergedProducts(): LiveData<UiState<List<Product>>> {
        val mergedLiveData = MediatorLiveData<UiState<List<Product>>>()

        // Step 1: Observe local data and emit it immediately when it becomes available
        val localData = databaseRepository.getAllProduct()
        mergedLiveData.addSource(localData) { products ->
            if (!products.isNullOrEmpty()) {
                mergedLiveData.value = UiState.Success(products)
            }
        }

        // Step 2: Fetch remote data and update the LiveData accordingly
        viewModelScope.launch {
            // Emit loading state after local data has been set
            if (localData.value.isNullOrEmpty()) {
                mergedLiveData.postValue(UiState.Loading)
            }

            // Fetch remote data
            when (val result = repository.getProducts()) {
                is RepositoryResponse.Success -> {
                    // Emit remote data if successful
                    mergedLiveData.postValue(UiState.Success(result.data.products))
                }

                is RepositoryResponse.Error, is RepositoryResponse.ApiError -> {
                    // If remote fetch fails, emit an error state but keep the local data
                    mergedLiveData.postValue(
                        UiState.DisplayError(
                            (result as? RepositoryResponse.Error)?.error
                                ?: (result as RepositoryResponse.ApiError).apiError
                        )
                    )
                }
            }
        }

        return mergedLiveData
    }

    fun saveToDb(entity: Product) {
        viewModelScope.launch {
            databaseRepository.saveToDb(entity)
        }
    }

    fun getAllProductInCart(): LiveData<List<Product>> {
        return databaseRepository.getAllProductInCart()
    }
}