package com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.checkout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper
import com.kelviniyalo.youverifystorekelviniyalo.databinding.FragmentCheckOutBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CheckOutFragment : Fragment(R.layout.fragment_check_out) {

    private lateinit var binding: FragmentCheckOutBinding
    private val viewModel: ConfirmOrderModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCheckOutBinding.bind(view)
        observeViewModel()
        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            confirmOrderBtn.setOnClickListener { confirmOrder() }
        }
    }

    private fun observeViewModel() {
        viewModel.totalAmountInCart().observe(viewLifecycleOwner, Observer { amount ->
            if (amount != null) {
                computeOrderAmount(amount)
            }
        })
    }

    private fun confirmOrder() {
        Helper.showSuccessAlertDialog(requireActivity(), message = "Your order has been placed",onContinue = {
            viewModel.clearCart()
            findNavController().popBackStack(
                R.id.homeFragment,
                false
            )
        })
    }

    private fun computeOrderAmount(amount: Float) {
        val deliveryAmount = amount * 0.1f
        val serviceAmount = amount * 0.08f
        val total = amount + deliveryAmount + serviceAmount

        with(binding) {
            productValue.text = formatCurrency(amount)
            serviceValue.text = formatCurrency(serviceAmount)
            deliveryFeeValue.text = formatCurrency(deliveryAmount)
            totaleValue.text = formatCurrency(total)
        }
    }

    private fun formatCurrency(value: Float): String {
        return activity?.getString(R.string.naira, Helper.formatAmount(value.toString())).orEmpty()
    }
}