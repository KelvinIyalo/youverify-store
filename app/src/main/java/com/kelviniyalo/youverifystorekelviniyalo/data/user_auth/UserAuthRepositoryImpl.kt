package com.kelviniyalo.youverifystorekelviniyalo.data.user_auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kelviniyalo.youverifystorekelviniyalo.domain.user_auth.UserAuthRepository

class UserAuthRepositoryImpl : UserAuthRepository {

    override fun userLogin(email: String, password: String) : Task<AuthResult> {
     return FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
    }

    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override suspend fun userRegister(email: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
    }

    override fun userLogout() {
        FirebaseAuth.getInstance().signOut()
    }
}