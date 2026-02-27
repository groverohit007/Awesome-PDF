package com.awesomepdf.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomepdf.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _status = MutableStateFlow("Not signed in")
    val status = _status.asStateFlow()

    fun signInWithGoogle(idToken: String, onDone: () -> Unit) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            _status.value = if (result.isSuccess) "Google sign-in success" else "Google sign-in failed"
            if (result.isSuccess) onDone()
        }
    }

    fun skip(onDone: () -> Unit) {
        viewModelScope.launch {
            val result = authRepository.skipSignIn()
            _status.value = if (result.isSuccess) "Signed in anonymously" else "Anonymous sign-in failed"
            if (result.isSuccess) onDone()
        }
    }
}
