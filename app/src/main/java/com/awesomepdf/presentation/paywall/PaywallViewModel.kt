package com.awesomepdf.presentation.paywall

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomepdf.BuildConfig
import com.awesomepdf.data.billing.EntitlementManager
import com.awesomepdf.domain.model.EntitlementState
import com.awesomepdf.domain.repository.AuthRepository
import com.awesomepdf.domain.repository.BillingRepository
import com.awesomepdf.domain.usecase.GetEntitlementUseCase
import com.awesomepdf.domain.usecase.SetDebugEntitlementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PaywallViewModel @Inject constructor(
    getEntitlementUseCase: GetEntitlementUseCase,
    private val billingRepository: BillingRepository,
    private val setDebugEntitlementUseCase: SetDebugEntitlementUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    val entitlementState = getEntitlementUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EntitlementState())

    val debugEnabled = BuildConfig.ENABLE_FAKE_ENTITLEMENT

    init {
        viewModelScope.launch { billingRepository.refreshEntitlement() }
        viewModelScope.launch {
            entitlementState.collect { state ->
                authRepository.syncUserSettings(
                    mapOf(
                        "entitlementTier" to state.tier.name,
                        "isPremium" to state.isPremium,
                        "entitlementSource" to state.source
                    )
                )
            }
        }
    }

    fun buy(activity: Activity, productId: String) {
        viewModelScope.launch {
            billingRepository.launchPurchase(activity, productId)
        }
    }

    fun restore() {
        viewModelScope.launch { billingRepository.restorePurchases() }
    }

    fun fakePremium(enabled: Boolean) {
        if (!debugEnabled) return
        viewModelScope.launch { setDebugEntitlementUseCase(enabled) }
    }

    fun fakeLifetime() = fakePremium(true)

    companion object {
        val plans = listOf(
            EntitlementManager.PRODUCT_MONTHLY to "Monthly",
            EntitlementManager.PRODUCT_YEARLY to "Yearly",
            EntitlementManager.PRODUCT_LIFETIME to "Lifetime"
        )
    }
}
