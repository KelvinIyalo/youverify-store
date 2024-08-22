package com.kelviniyalo.youverifystorekelviniyalo.presentation.auth.user_profile

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper.showMessage
import com.kelviniyalo.youverifystorekelviniyalo.databinding.FragmentUserProfileBinding
import com.kelviniyalo.youverifystorekelviniyalo.presentation.auth.auth_host.AuthActivity
import com.kelviniyalo.youverifystorekelviniyalo.presentation.main_app.main_host.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {
    private val viewModel:UserProfileViewModel by viewModels()
    private lateinit var binding: FragmentUserProfileBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserProfileBinding.bind(view)
        updateUi()
        manageTransitionAnimation()
    }

    private fun updateUi(){
        with(binding){
            val profile = viewModel.getCurrentUser()
            emailValue.text = profile?.email
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            logOut.setOnClickListener {
            it.showMessage("user has been logged out!!")
                viewModel.userLogout()
                navigateToAuth()
            }
        }
    }

    private fun manageTransitionAnimation() {
        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    private fun navigateToAuth(){
        startActivity(Intent(requireContext(), AuthActivity::class.java))
        requireActivity().finish()
    }
}