package com.awesomepdf.data.billing

import com.awesomepdf.domain.model.Entitlement
import com.awesomepdf.domain.model.PlanType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntitlementManager @Inject constructor() {
    private val _entitlement = MutableStateFlow(Entitlement())
    val entitlement: StateFlow<Entitlement> = _entitlement.asStateFlow()

    fun updateFromPurchase(productId: String?) {
        val plan = when (productId) {
            PRODUCT_MONTHLY -> PlanType.MONTHLY
            PRODUCT_YEARLY -> PlanType.YEARLY
            PRODUCT_LIFETIME -> PlanType.LIFETIME
            else -> PlanType.FREE
        }
        _entitlement.value = Entitlement(plan, plan != PlanType.FREE, "billing")
    }

    fun setDebug(enabled: Boolean) {
        _entitlement.value = if (enabled) {
            Entitlement(PlanType.LIFETIME, active = true, source = "debug")
        } else {
            Entitlement()
        }
    }

    companion object {
        const val PRODUCT_MONTHLY = "awesomepdf_monthly"
        const val PRODUCT_YEARLY = "awesomepdf_yearly"
        const val PRODUCT_LIFETIME = "awesomepdf_lifetime"
    }
}
