package com.alekseyvalyakin.roleplaysystem.data.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthProviderImpl @Inject constructor() : AuthProvider {
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun login(email: String, password: String): Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithEmailAndPassword(FirebaseAuth.getInstance(), email, password)
    }

    override fun signUp(email: String, password: String): Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(FirebaseAuth.getInstance(), email, password)
    }
}

interface AuthProvider {
    fun login(email: String, password: String): Maybe<AuthResult>

    fun signUp(email: String, password: String): Maybe<AuthResult>

    fun getCurrentUser(): FirebaseUser?
}