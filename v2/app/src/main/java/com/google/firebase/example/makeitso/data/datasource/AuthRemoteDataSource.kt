package com.google.firebase.example.makeitso.data.datasource

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val auth: FirebaseAuth) {
    val currentUser: FirebaseUser? get() = auth.currentUser

    val currentUserIdFlow: Flow<String?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { _ -> this.trySend(currentUser?.uid) }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    suspend fun createGuestAccount() {
        auth.signInAnonymously().await()
    }

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun linkAccount(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).await()
    }

    suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    suspend fun signOut(context: Context) {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
        }
        auth.signOut()

        val credentialManager = CredentialManager.create(context)
        val clearRequest = ClearCredentialStateRequest()
        credentialManager.clearCredentialState(clearRequest)
    }

    suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }
}