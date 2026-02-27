package com.awesomepdf.presentation.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomepdf.BuildConfig
import com.awesomepdf.domain.model.EntitlementState
import com.awesomepdf.domain.repository.BillingRepository
import com.awesomepdf.domain.usecase.GetEntitlementUseCase
import com.awesomepdf.domain.usecase.SetDebugEntitlementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getEntitlementUseCase: GetEntitlementUseCase,
    private val setDebugEntitlementUseCase: SetDebugEntitlementUseCase,
    private val billingRepository: BillingRepository
) : ViewModel() {

    val entitlementState = getEntitlementUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EntitlementState())

    val debugEnabled = BuildConfig.ENABLE_FAKE_ENTITLEMENT

    fun toggleDebugEntitlement(enabled: Boolean) {
        if (!debugEnabled) return
        viewModelScope.launch { setDebugEntitlementUseCase(enabled) }
    }

    fun restorePurchases() {
        viewModelScope.launch { billingRepository.restorePurchases() }
    }
}
