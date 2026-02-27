package com.awesomepdf.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomepdf.domain.model.EntitlementState
import com.awesomepdf.domain.usecase.GetEntitlementUseCase
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    getEntitlementUseCase: GetEntitlementUseCase
) : ViewModel() {

    val entitlementState: StateFlow<EntitlementState> =
        getEntitlementUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EntitlementState())

    val showPaywallBanner: StateFlow<Boolean> = entitlementState
        .map {
            val enabled = Firebase.remoteConfig.getBoolean("paywall_banner_enabled")
            enabled && !it.isPremium
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}
