package com.awesomepdf.data.billing

import com.awesomepdf.domain.model.EntitlementState
import com.awesomepdf.domain.model.EntitlementTier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntitlementManager @Inject constructor() {
    private val _entitlementState = MutableStateFlow(EntitlementState())
    val entitlementState: StateFlow<EntitlementState> = _entitlementState.asStateFlow()

    fun updateFromPurchase(productId: String?) {
        val tier = when (productId) {
            PRODUCT_MONTHLY -> EntitlementTier.PREMIUM_MONTHLY
            PRODUCT_YEARLY -> EntitlementTier.PREMIUM_YEARLY
            PRODUCT_LIFETIME -> EntitlementTier.PREMIUM_LIFETIME
            else -> EntitlementTier.FREE
        }
        _entitlementState.value = EntitlementState(tier, tier != EntitlementTier.FREE, "billing")
    }

    fun setDebug(enabled: Boolean) {
        _entitlementState.value = if (enabled) {
            EntitlementState(EntitlementTier.PREMIUM_LIFETIME, isPremium = true, source = "debug")
        } else {
            EntitlementState()
        }
    }

    companion object {
        const val PRODUCT_MONTHLY = "awesomepdf_monthly"
        const val PRODUCT_YEARLY = "awesomepdf_yearly"
        const val PRODUCT_LIFETIME = "awesomepdf_lifetime"

        val SUBS_PRODUCTS = listOf(PRODUCT_MONTHLY, PRODUCT_YEARLY)
        val INAPP_PRODUCTS = listOf(PRODUCT_LIFETIME)
    }
}
