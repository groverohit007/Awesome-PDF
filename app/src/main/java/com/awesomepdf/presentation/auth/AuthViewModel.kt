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

    fun signInWithGoogleStub(onDone: () -> Unit) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle("stub-google-id-token")
            _status.value = if (result.isSuccess) "Google sign-in success" else "Google sign-in failed, use Skip"
            if (result.isSuccess) onDone()
        }
    }

    fun skip(onDone: () -> Unit) {
        viewModelScope.launch {
            authRepository.skipSignIn()
            _status.value = "Signed in anonymously"
            onDone()
        }
    }
}
