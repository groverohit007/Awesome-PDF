package com.awesomepdf.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomepdf.presentation.main.settings.SettingsViewModel

@Composable
fun FeatureGate(
    lockedMessage: String,
    onOpenPaywall: () -> Unit,
    content: @Composable () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val entitlement by viewModel.entitlementState.collectAsState()
    if (entitlement.isPremium) {
        content()
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(lockedMessage)
        Button(onClick = onOpenPaywall, modifier = Modifier.padding(top = 12.dp)) {
            Text("Go Premium")
        }
    }
}
