package com.awesomepdf.domain.usecase

import com.awesomepdf.domain.model.Entitlement
import com.awesomepdf.domain.repository.BillingRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetEntitlementUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    operator fun invoke(): StateFlow<Entitlement> = billingRepository.entitlement
}
