package com.kelviniyalo.youverifystorekelviniyalo.presentation.auth.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper.showMessage
import com.kelviniyalo.youverifystorekelviniyalo.common.handler.UiState
import com.kelviniyalo.youverifystorekelviniyalo.databinding.FragmentSignInBinding
import com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.main_host.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: UserLoginViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        textWatcher()
        validateUserLogin()
        with(binding) {
            registerLabel.setOnClickListener {
                val direction =
                    SignInFragmentDirections.actionSignInFragmentToRegistrationFragment()
                val extras = FragmentNavigatorExtras(
                    binding.userLoginCard to "registration_card",
                    binding.loginBtn to "registration_button",
                    binding.loginImage to "registration_image",
                    binding.loginLabel to "registration_label"
                )
                findNavController().navigate(direction, navigatorExtras = extras)
            }

            loginBtn.setOnClickListener { userLogin() }
        }
    }

    private fun userLogin() {

        viewModel.userLogin(
            binding.emailEt.text.toString(),
            binding.passwordEt.text.toString()
        )

        viewModel.transactionListener.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is UiState.Loading -> {
                    showLoading(true)
                }

                is UiState.Success -> {
                    showLoading(false)
                    navigateToMainApp()
                }

                is UiState.DisplayError -> {
                    showLoading(false)
                    binding.root.showMessage( response.error)
                    showLoading(false)
                }

            }
        })
    }

    private fun validateUserInput() {
        with(binding) {
            val isButtonEnable = emailEt.text.isNotEmpty() && passwordEt.text.isNotEmpty()
            loginBtn.isEnabled = isButtonEnable
        }
    }

    private fun textWatcher() {
        with(binding) {
            emailEt.addTextChangedListener { validateUserInput() }
            passwordEt.addTextChangedListener { validateUserInput() }
        }
    }

    private fun showLoading(isLoading:Boolean = false){
       binding.progressBar.isVisible = isLoading
    }
    private fun validateUserLogin(){
        if (!viewModel.getCurrentUser()?.uid.isNullOrEmpty()){
            navigateToMainApp()
        }
    }

    private fun navigateToMainApp(){
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }
}