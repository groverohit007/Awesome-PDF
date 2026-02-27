package com.awesomepdf.domain.usecase

import com.awesomepdf.domain.model.EntitlementState
import com.awesomepdf.domain.repository.BillingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEntitlementUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    operator fun invoke(): Flow<EntitlementState> = billingRepository.entitlementState
}
