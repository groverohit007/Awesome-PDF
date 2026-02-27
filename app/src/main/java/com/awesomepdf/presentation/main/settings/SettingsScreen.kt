package com.awesomepdf.presentation.main.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(onSignedOut: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val entitlement by viewModel.entitlementState.collectAsState()
    val debugToggle = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Settings")
        Text("Entitlement: ${entitlement.tier} (${entitlement.source})")

        Button(onClick = { viewModel.restorePurchases() }) {
            Text("Restore Purchases")
        }

        Button(onClick = { viewModel.signOut(); onSignedOut() }) {
            Text("Sign Out")
        }

        if (viewModel.debugEnabled) {
            Text("Debug fake entitlement")
            Switch(
                checked = debugToggle.value,
                onCheckedChange = {
                    debugToggle.value = it
                    viewModel.toggleDebugEntitlement(it)
                }
            )
        }
    }
}
