package com.google.firebase.example.makeitso.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.example.makeitso.data.datasource.AuthRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser? = authRemoteDataSource.currentUser
    val currentUserIdFlow: Flow<String?> = authRemoteDataSource.currentUserIdFlow

    suspend fun createGuestAccount() {
        authRemoteDataSource.createGuestAccount()
    }

    suspend fun signIn(email: String, password: String) {
        authRemoteDataSource.signIn(email, password)
    }

    suspend fun signUp(email: String, password: String) {
        authRemoteDataSource.linkAccount(email, password)
    }

    suspend fun signInWithGoogle(idToken: String) {
        authRemoteDataSource.signInWithGoogle(idToken)
    }

    suspend fun signOut(context: Context) {
        authRemoteDataSource.signOut(context)
    }

    suspend fun deleteAccount() {
        authRemoteDataSource.deleteAccount()
    }
}