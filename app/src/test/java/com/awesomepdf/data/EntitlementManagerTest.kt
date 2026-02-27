package com.awesomepdf.data

import com.awesomepdf.data.billing.EntitlementManager
import com.awesomepdf.domain.model.PlanType
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EntitlementManagerTest {

    @Test
    fun `updateFromPurchase maps product ids to plan`() {
        val manager = EntitlementManager()

        manager.updateFromPurchase(EntitlementManager.PRODUCT_YEARLY)

        assertThat(manager.entitlement.value.planType).isEqualTo(PlanType.YEARLY)
        assertThat(manager.entitlement.value.active).isTrue()
    }
}
