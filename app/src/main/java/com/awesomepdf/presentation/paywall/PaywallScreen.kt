package com.awesomepdf.presentation.paywall

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PaywallScreen(
    onBack: () -> Unit,
    viewModel: PaywallViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val entitlement by viewModel.entitlementState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Go Premium")
        Text("Current: ${entitlement.tier}")

        PaywallViewModel.plans.forEach { (productId, label) ->
            Button(onClick = { activity?.let { viewModel.buy(it, productId) } }) {
                Text(label)
            }
        }

        Button(onClick = { viewModel.restore() }) {
            Text("Restore Purchases")
        }

        if (viewModel.debugEnabled) {
            Button(onClick = { viewModel.fakeLifetime() }) {
                Text("Debug: Unlock Premium")
            }
        }

        Button(onClick = onBack) { Text("Back") }
    }
}
