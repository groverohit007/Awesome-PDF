package com.awesomepdf.domain.model

enum class PlanType { FREE, MONTHLY, YEARLY, LIFETIME }

data class Entitlement(
    val planType: PlanType = PlanType.FREE,
    val active: Boolean = false,
    val source: String = "none"
)
