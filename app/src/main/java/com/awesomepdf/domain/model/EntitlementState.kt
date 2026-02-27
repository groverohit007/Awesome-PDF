package com.awesomepdf.domain.model

enum class EntitlementTier { FREE, PREMIUM_MONTHLY, PREMIUM_YEARLY, PREMIUM_LIFETIME }

data class EntitlementState(
    val tier: EntitlementTier = EntitlementTier.FREE,
    val isPremium: Boolean = false,
    val source: String = "none"
)
