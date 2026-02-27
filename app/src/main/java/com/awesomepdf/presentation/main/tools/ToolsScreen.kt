package com.awesomepdf.presentation.main.tools

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ToolsScreen() {
    val selectedFiles = remember { mutableStateOf<List<Uri>>(emptyList()) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { selectedFiles.value = selectedFiles.value + it }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Tools")
        Button(onClick = { launcher.launch(arrayOf("application/pdf")) }) { Text("Merge PDFs (pick file)") }
        Button(onClick = { launcher.launch(arrayOf("application/pdf")) }) { Text("Split PDF (pick file)") }
        Button(onClick = { launcher.launch(arrayOf("application/pdf")) }) { Text("Compress PDF (pick file)") }
        Button(onClick = { launcher.launch(arrayOf("image/*")) }) { Text("Images to PDF (pick image)") }
        Text("Selected inputs: ${selectedFiles.value.size}")
        Text("Processing runs as background work stubs.")
    }
}
