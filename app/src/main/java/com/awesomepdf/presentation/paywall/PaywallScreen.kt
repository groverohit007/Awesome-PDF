package com.awesomepdf.presentation.paywall

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomepdf.data.billing.EntitlementManager

@Composable
fun PaywallScreen(onBack: () -> Unit, viewModel: PaywallViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Go Premium")
        Button(onClick = { viewModel.fakePurchase(EntitlementManager.PRODUCT_MONTHLY) }) { Text("Monthly Subscription") }
        Button(onClick = { viewModel.fakePurchase(EntitlementManager.PRODUCT_YEARLY) }) { Text("Yearly Subscription") }
        Button(onClick = { viewModel.fakePurchase(EntitlementManager.PRODUCT_LIFETIME) }) { Text("Lifetime") }
        Button(onClick = onBack) { Text("Back") }
    }
}
