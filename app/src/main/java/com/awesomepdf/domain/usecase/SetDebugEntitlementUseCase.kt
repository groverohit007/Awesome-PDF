package com.awesomepdf.domain.usecase

import com.awesomepdf.domain.repository.BillingRepository
import javax.inject.Inject

class SetDebugEntitlementUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(enabled: Boolean) = billingRepository.setDebugEntitlement(enabled)
}
