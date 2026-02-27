package com.awesomepdf.presentation.main.ai

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.awesomepdf.presentation.common.FeatureGate

@Composable
fun AiScreen(onOpenPaywall: () -> Unit) {
    FeatureGate(
        lockedMessage = "AI tools are Premium. Upgrade to continue.",
        onOpenPaywall = onOpenPaywall
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("AI Premium Workspace")
        }
    }
}
