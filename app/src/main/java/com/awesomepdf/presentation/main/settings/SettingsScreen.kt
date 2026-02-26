package com.awesomepdf.presentation.main.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val entitlement by viewModel.entitlement.collectAsState()
    val debug = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Settings")
        Text("Current plan: ${entitlement.planType} (${entitlement.source})")
        if (viewModel.debugEnabled) {
            Text("Debug entitlement override")
            Switch(
                checked = debug.value,
                onCheckedChange = {
                    debug.value = it
                    viewModel.toggleDebugEntitlement(it)
                }
            )
        }
    }
}
