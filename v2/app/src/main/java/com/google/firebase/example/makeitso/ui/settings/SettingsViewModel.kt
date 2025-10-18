package com.google.firebase.example.makeitso.ui.settings

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.example.makeitso.MainViewModel
import com.google.firebase.example.makeitso.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : MainViewModel() {
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean>
        get() = _shouldRestartApp.asStateFlow()

    private val _isAnonymous = MutableStateFlow(true)
    val isAnonymous: StateFlow<Boolean>
        get() = _isAnonymous.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String>
        get() = _email.asStateFlow()

    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String>
        get() = _displayName.asStateFlow()

    private val _authMethod = MutableStateFlow("")
    val authMethod: StateFlow<String>
        get() = _authMethod.asStateFlow()

    fun loadCurrentUser() {
        launchCatching {
            val currentUser = authRepository.currentUser ?: return@launchCatching
            _isAnonymous.value = currentUser.isAnonymous
            if (!currentUser.isAnonymous) {
                _email.value = currentUser.email ?: ""
                _displayName.value = currentUser.displayName ?: ""
                _authMethod.value = if (currentUser.providerData.any { it.providerId == "google.com" }) {
                    "Google"
                } else if (currentUser.providerData.any { it.providerId == "password" }) {
                    "Email"
                } else {
                    "Unknown"
                }
            }
        }
    }

    fun signOut(context: Context) {
        launchCatching {
            authRepository.signOut(context)
            _shouldRestartApp.value = true
        }
    }

    fun deleteAccount() {
        launchCatching {
            authRepository.deleteAccount()
            _shouldRestartApp.value = true
        }
    }
}