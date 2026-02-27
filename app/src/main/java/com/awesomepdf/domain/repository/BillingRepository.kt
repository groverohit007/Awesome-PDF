package com.awesomepdf.domain.repository

import android.app.Activity
import com.awesomepdf.domain.model.EntitlementState
import kotlinx.coroutines.flow.Flow

interface BillingRepository {
    val entitlementState: Flow<EntitlementState>
    suspend fun refreshEntitlement()
    suspend fun launchPurchase(activity: Activity, productId: String): Result<Unit>
    suspend fun restorePurchases(): Result<Unit>
    suspend fun setDebugEntitlement(enabled: Boolean)
}
