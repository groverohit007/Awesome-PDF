package com.awesomepdf.data

import com.awesomepdf.data.billing.EntitlementManager
import com.awesomepdf.domain.model.EntitlementTier
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EntitlementManagerTest {

    @Test
    fun `updateFromPurchase maps product ids to premium tier`() {
        val manager = EntitlementManager()

        manager.updateFromPurchase(EntitlementManager.PRODUCT_YEARLY)

        assertThat(manager.entitlementState.value.tier).isEqualTo(EntitlementTier.PREMIUM_YEARLY)
        assertThat(manager.entitlementState.value.isPremium).isTrue()
    }
}
