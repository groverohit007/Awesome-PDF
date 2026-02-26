package com.awesomepdf.presentation.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomepdf.BuildConfig
import com.awesomepdf.domain.usecase.GetEntitlementUseCase
import com.awesomepdf.domain.usecase.SetDebugEntitlementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getEntitlementUseCase: GetEntitlementUseCase,
    private val setDebugEntitlementUseCase: SetDebugEntitlementUseCase
) : ViewModel() {
    val entitlement = getEntitlementUseCase()
    val debugEnabled = BuildConfig.ENABLE_FAKE_ENTITLEMENT

    fun toggleDebugEntitlement(enabled: Boolean) {
        if (!debugEnabled) return
        viewModelScope.launch { setDebugEntitlementUseCase(enabled) }
    }
}
