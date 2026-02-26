package com.awesomepdf.presentation.paywall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomepdf.data.billing.EntitlementManager
import com.awesomepdf.domain.usecase.SetDebugEntitlementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val setDebugEntitlementUseCase: SetDebugEntitlementUseCase
) : ViewModel() {
    fun fakePurchase(productId: String) {
        viewModelScope.launch {
            setDebugEntitlementUseCase(productId == EntitlementManager.PRODUCT_LIFETIME)
        }
    }
}
