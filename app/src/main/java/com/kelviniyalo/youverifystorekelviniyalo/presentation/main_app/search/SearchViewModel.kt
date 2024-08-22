package com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.domain.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) :
    ViewModel() {

    fun searchProductsFromLocalStorage(
        searchString: String
    ):LiveData<List<Product>> {

        val mergedLiveData = MediatorLiveData<List<Product>>()

        // Step 1: Observe local data and emit it immediately when it becomes available
        mergedLiveData.addSource(databaseRepository.getAllProduct()) { products ->
            if (!products.isNullOrEmpty()) {
                val result = products.filter { transaction ->
                    searchFilterPredicate(searchString, transaction)
                }
                mergedLiveData.value = result
            }
        }
       return mergedLiveData
    }

    private fun searchFilterPredicate(
        searchQuery: String,
        product: Product
    ): Boolean {
        val matchesTitle = product.title?.contains(searchQuery, true) == true
        val matchesDescription = product.description?.contains(searchQuery, true) == true
        val matchesCategory = product.category?.contains(searchQuery, true) == true
        val matchesAmount = product.price.toString()
            .contains(searchQuery, true)
        return matchesTitle || matchesDescription || matchesCategory || matchesAmount
    }
}