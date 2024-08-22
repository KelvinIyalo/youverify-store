package com.kelviniyalo.youverifystorekelviniyalo.presentation.auth.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelviniyalo.youverifystorekelviniyalo.common.handler.UiState
import com.kelviniyalo.youverifystorekelviniyalo.data.model.AuthResponseHandler
import com.kelviniyalo.youverifystorekelviniyalo.domain.user_auth.UserAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRegistrationViewModel @Inject constructor(
    private val authRepository: UserAuthRepository
) :
    ViewModel() {

    val transactionListener = MutableLiveData<UiState<AuthResponseHandler>>()
    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            transactionListener.value = UiState.Loading

            val result = authRepository.userRegister(email, password)
            result.addOnCompleteListener {
                if (it.isSuccessful) {
                    transactionListener.value =
                        UiState.Success(AuthResponseHandler(it.isSuccessful))
                }
            }
                .addOnFailureListener {
                    if (it.message?.isNotEmpty() == true) {
                        transactionListener.value = UiState.DisplayError(it.message?.replace("[","")?.replace("]","")
                            .toString())
                    }
                }
        }


    }
}