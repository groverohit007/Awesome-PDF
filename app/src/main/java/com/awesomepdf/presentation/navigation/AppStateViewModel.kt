package com.awesomepdf.presentation.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppStateViewModel @Inject constructor() : ViewModel() {
    private val _completedOnboarding = MutableStateFlow(false)
    val completedOnboarding: StateFlow<Boolean> = _completedOnboarding.asStateFlow()

    fun completeOnboarding() {
        _completedOnboarding.value = true
    }
}
