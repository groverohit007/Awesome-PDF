package com.awesomepdf.data.billing

import android.app.Activity
import com.awesomepdf.domain.model.EntitlementState
import com.awesomepdf.domain.repository.BillingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingRepositoryImpl @Inject constructor(
    private val entitlementManager: EntitlementManager,
    private val billingClientManager: BillingClientManager
) : BillingRepository {

    override val entitlementState: Flow<EntitlementState> = entitlementManager.entitlementState

    override suspend fun refreshEntitlement() {
        billingClientManager.refreshEntitlement()
    }

    override suspend fun launchPurchase(activity: Activity, productId: String): Result<Unit> {
        return billingClientManager.launchPurchase(activity, productId)
    }

    override suspend fun restorePurchases(): Result<Unit> {
        return billingClientManager.restorePurchases()
    }

    override suspend fun setDebugEntitlement(enabled: Boolean) {
        entitlementManager.setDebug(enabled)
    }
}
