package com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.search

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper.startSlideInAnimation
import com.kelviniyalo.youverifystorekelviniyalo.data.adapter.ProductsAdapter
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchProductFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var productsAdapter: ProductsAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        initTransactionsRecyclerViewAdapter()
        with(binding) {

            searchEt.apply {
                requestFocus()
                setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        val inputMethodManager =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
                    }
                }
                addTextChangedListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(300)
                        searchProduct(it.toString().trim())
                    }
                }
            }
            backBtn.startSlideInAnimation(requireContext())
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun searchProduct(name: String) {
        viewModel.searchProductsFromLocalStorage(name)
            .observe(viewLifecycleOwner, Observer { response ->
                productsAdapter.submitList(response)
            })
    }


    private fun initTransactionsRecyclerViewAdapter() {
        productsAdapter = ProductsAdapter(
            onItemClicked = { itemAtPosition ->
                manageItemClick(itemId = itemAtPosition)
            }
        )
        binding.searchListRv.apply {
            adapter = productsAdapter
            layoutManager = Helper.createGridLayoutManager(requireContext(), productsAdapter)
        }
    }

    private fun manageItemClick(itemId: Product) {
        val direction =
            SearchProductFragmentDirections.actionSearchProductFragmentToProductDetailFragment(
                itemId
            )
        findNavController().navigate(
            direction
        )
    }
}