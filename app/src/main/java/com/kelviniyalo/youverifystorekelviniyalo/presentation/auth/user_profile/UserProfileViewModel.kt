package com.kelviniyalo.youverifystorekelviniyalo.presentation.auth.user_profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.kelviniyalo.youverifystorekelviniyalo.domain.user_auth.UserAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val authRepository: UserAuthRepository):ViewModel() {


    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

    fun userLogout(){
        authRepository.userLogout()
    }
}