package com.awesomepdf.presentation.main.home

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    onOpenPaywall: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val showBanner by viewModel.showPaywallBanner.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Home")
        if (showBanner) {
            Text("Premium unlock: AI tools and advanced PDF features")
            Button(onClick = onOpenPaywall) {
                Text("See Plans")
            }
        }
    }
}
