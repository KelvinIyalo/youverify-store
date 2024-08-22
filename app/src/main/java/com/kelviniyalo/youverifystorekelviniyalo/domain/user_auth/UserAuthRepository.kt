package com.kelviniyalo.youverifystorekelviniyalo.domain.user_auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface UserAuthRepository {

    fun userLogin(email:String,password:String) : Task<AuthResult>

    fun getCurrentUser() : FirebaseUser?

    suspend fun userRegister(email:String,password:String) : Task<AuthResult>

    fun userLogout()
}