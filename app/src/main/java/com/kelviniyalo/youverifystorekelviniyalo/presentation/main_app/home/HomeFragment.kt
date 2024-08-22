package com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper.showMessage
import com.kelviniyalo.youverifystorekelviniyalo.common.handler.UiState
import com.kelviniyalo.youverifystorekelviniyalo.data.adapter.ProductsAdapter
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var productsAdapter: ProductsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        setupRecyclerView()
        observeViewModel()
        navigateToCart()
        navigateToSearch()



    }
    private fun observeViewModel() {
        viewModel.apply {

            getMergedProducts().observe(viewLifecycleOwner) { response ->
                handleProductResponse(response)
            }

            getAllProductInCart().observe(viewLifecycleOwner) { cartItems ->
                updateCartLabel(cartItems.size)
            }

        }
    }

    private fun handleProductResponse(response: UiState<List<Product>>) {
        when (response) {

            is UiState.Loading -> showLoader(true)

            is UiState.Success -> {
                showLoader(false)
                productsAdapter.submitList(response.data)
            }

            is UiState.DisplayError -> {
                showLoader(false)
                binding.root.showMessage(response.error)
            }

        }
    }

    private fun setupRecyclerView() {
        productsAdapter = ProductsAdapter(
            onListItems = viewModel::saveToDb,
            onItemClicked = ::manageItemClick
        )

        binding.productListRv.apply {
            adapter = productsAdapter
            layoutManager = Helper.createGridLayoutManager(requireContext(),productsAdapter)
        }
    }



    private fun updateCartLabel(cartItemCount: Int) {
        binding.cartLabel.apply {
            isVisible = cartItemCount > 0
            text = cartItemCount.toString()
        }
    }

    private fun showLoader(isLoading: Boolean) {
        binding.loader.isVisible = isLoading
    }

    private fun manageItemClick(product: Product) {
        val direction = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(product)
        findNavController().navigate(direction)
    }

    private fun navigateToCart(){
        binding.viewCart.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.toolbarSection to "toolbar_cart",
                binding.productListRv to "rv_cart"
            )
            val direction = HomeFragmentDirections.actionHomeFragmentToCartFragment()
            findNavController().navigate(direction,extras)
        }
    }

    private fun navigateToSearch(){
        binding.searchEt.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.toolbarSection to "search_toolbar",
                binding.searchEt to "search_search"
            )

            val direction = HomeFragmentDirections.actionHomeFragmentToSearchProductFragment()
            findNavController().navigate(
                direction,
                extras
            )
        }
    }
}