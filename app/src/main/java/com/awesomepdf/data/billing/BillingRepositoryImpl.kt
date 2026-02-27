package com.awesomepdf.data.billing

import com.awesomepdf.domain.model.Entitlement
import com.awesomepdf.domain.repository.BillingRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingRepositoryImpl @Inject constructor(
    private val entitlementManager: EntitlementManager
) : BillingRepository {
    override val entitlement: StateFlow<Entitlement> = entitlementManager.entitlement

    override suspend fun refreshEntitlement() {
        entitlementManager.updateFromPurchase(null)
    }

    override suspend fun setDebugEntitlement(enabled: Boolean) {
        entitlementManager.setDebug(enabled)
    }
}
