package com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.cart

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper
import com.kelviniyalo.youverifystorekelviniyalo.data.adapter.CartItemsAdapter
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartItemsAdapter: CartItemsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)
        setupRecyclerView()
        observeViewModel()
        manageTransitionAnimation()
        onBackPressed()

        with(binding) {
            checkoutBtn.setOnClickListener { checkoutCart() }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack(
                    R.id.homeFragment,
                    false
                )
            }
        }

    }

    private fun observeViewModel() {
        viewModel.getAllProductInCart().observe(viewLifecycleOwner, Observer { response ->
            cartItemsAdapter.submitList(response)
        })

        viewModel.totalAmountInCart().observe(viewLifecycleOwner, Observer { amount ->
            val isAmountEmpty = amount.toString().toFloat() > 0
            binding.availableItems.isVisible = isAmountEmpty
            binding.emptyList.isVisible = !isAmountEmpty

            Log.d("XXXXX 2", amount.toString())

            binding.amount.text = activity?.getString(
                R.string.naira,
                Helper.formatAmount(amount.toString())
            )
        })
    }

    private fun setupRecyclerView() {
        cartItemsAdapter = CartItemsAdapter(
            onItemClicked = ::manageItemClick
        )
        binding.cartRv.adapter = cartItemsAdapter
    }

    private fun manageItemClick(product: Product) {
        viewModel.removeProductFromCart(product.id!!)
    }

    private fun manageTransitionAnimation() {
        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun checkoutCart() {
        val directions = CartFragmentDirections.actionCartFragmentToCheckOutFragment()
        findNavController().navigate(directions)
    }
}