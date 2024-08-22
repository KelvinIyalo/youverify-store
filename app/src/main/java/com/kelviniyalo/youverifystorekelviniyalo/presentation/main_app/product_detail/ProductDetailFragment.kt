package com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.product_detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper.showMessage
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper.startMoveUpAnimation
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.databinding.FragmentProductDetailsBinding
import com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.home.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_details) {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductDetailsViewModel by viewModels()

    private val navigationArgs: ProductDetailFragmentArgs by navArgs()
    private var quantity:Int? = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductDetailsBinding.bind(view)
        manageUiComponent(navigationArgs.productDetails)
        manageItemQuantity()
        addItemToShoppingCart()
        observeViewModel()

    }


    private fun manageUiComponent(product: Product) {
        with(binding) {
            val imageResult = Helper.byteArrayToBitmap(product.imageByteArray!!)
            productImg.setImageBitmap(imageResult)
            topLayout.setBackgroundColor(product.productColor)
            addToCart.backgroundTintList = ColorStateList.valueOf(product.productColor)
            cancelBtn.setOnClickListener { findNavController().popBackStack() }
            productTittle.text = product.title
            ratingBar.rating = product.rating?.toFloat()!!
            productDescription.text = product.description
            productAmount.text = activity?.getString(
                R.string.naira,
                Helper.formatAmount(product.price.toString())
            )
            productDescriptionCard.startMoveUpAnimation(requireContext())
            productAmountCard.startMoveUpAnimation(requireContext())
            topLayout.startMoveUpAnimation(requireContext())
            viewCart.setOnClickListener {
                findNavController().navigate(ProductDetailFragmentDirections.actionProductDetailFragmentToCartFragment())
            }
        }
    }

    private fun manageItemQuantity(){
        with(binding){
            positiveBtn.setOnClickListener {
                quantity = quantity!! + 1
                qty.text = quantity.toString()
            }
            negativeBtn.setOnClickListener {
                if (quantity!! > 1){
                    quantity = quantity!! - 1
                    qty.text = quantity.toString()
                }
            }
        }
    }

    private fun addItemToShoppingCart(){
        binding.addToCart.setOnClickListener {
            it.showMessage("${navigationArgs.productDetails.title} has been added to your cart")
            val entity = navigationArgs.productDetails.copy(isInCart = true, itemCount = quantity, itemAmount = (navigationArgs.productDetails.price?.times(
                quantity!!
            )))
            viewModel.updateCart(entity)
        }
    }

    private fun observeViewModel() {
        viewModel. getAllProductInCart().observe(viewLifecycleOwner) { cartItems ->
                updateCartLabel(cartItems.size)
            }


    }
    private fun updateCartLabel(cartItemCount: Int) {
        binding.cartLabel.apply {
            isVisible = cartItemCount > 0
            text = cartItemCount.toString()
        }
    }



}