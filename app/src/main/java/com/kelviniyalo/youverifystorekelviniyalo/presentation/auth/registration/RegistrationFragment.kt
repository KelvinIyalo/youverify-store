package com.kelviniyalo.youverifystorekelviniyalo.presentation.auth.registration

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper.showMessage
import com.kelviniyalo.youverifystorekelviniyalo.common.handler.UiState
import com.kelviniyalo.youverifystorekelviniyalo.databinding.FragmentRegistrationBinding
import com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.main_host.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel: UserRegistrationViewModel by viewModels()
    private lateinit var dialog: AlertDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegistrationBinding.bind(view)
        manageTransitionAnimation()
        textWatcher()
        binding.toolbar.setOnClickListener { findNavController().popBackStack() }
        binding.loginLabel.setOnClickListener { findNavController().popBackStack() }
        binding.registerBtn.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        viewModel.registerUser(
            binding.emailEt.text.toString(),
            binding.passwordEt.text.toString()
        )

        viewModel.transactionListener.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is UiState.Loading ->{
                   showLoading(true)
                }

                is UiState.Success ->{
                    showLoading(false)
                    Helper.showSuccessAlertDialog(requireActivity(), onContinue = {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()
                    })
                }

                is UiState.DisplayError ->{
                    showLoading(false)
                    binding.root.showMessage(response.error)
                    showLoading(false)
                }
            }
        })
    }

    private fun manageTransitionAnimation() {
        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    private fun validateUserInput() {
        with(binding) {
            val isInputFieldValid =
                emailEt.text.isNotEmpty() && passwordEt.text.isNotEmpty()
            val isButtonEnable =
                isInputFieldValid && (passwordEt.text.toString() == confirmPasswordEt.text.toString())
            registerBtn.isEnabled = isButtonEnable
        }
    }

    private fun textWatcher() {
        with(binding) {
            emailEt.addTextChangedListener { validateUserInput() }
            passwordEt.addTextChangedListener { validateUserInput() }
            confirmPasswordEt.addTextChangedListener { validateUserInput() }
        }
    }
    private fun showLoading(isLoading:Boolean = false){
        binding.progressBar.isVisible = isLoading
    }

}