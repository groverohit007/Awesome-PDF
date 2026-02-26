package com.awesomepdf.domain.repository

import com.awesomepdf.domain.model.Entitlement
import kotlinx.coroutines.flow.StateFlow

interface BillingRepository {
    val entitlement: StateFlow<Entitlement>
    suspend fun refreshEntitlement()
    suspend fun setDebugEntitlement(enabled: Boolean)
}
