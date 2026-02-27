package com.awesomepdf.presentation.main.tools

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ToolsScreen(
    onMergeCompleted: (String) -> Unit,
    viewModel: ToolsViewModel = hiltViewModel()
) {
    val selectedUris by viewModel.selectedUris.collectAsState()
    val isMerging by viewModel.isMerging.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val resultPath by viewModel.resultPath.collectAsState()
    val error by viewModel.error.collectAsState()

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris: List<Uri> ->
        viewModel.onPdfSelection(uris.map { it.toString() })
    }

    LaunchedEffect(resultPath) {
        resultPath?.let(onMergeCompleted)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Merge PDFs")
        Text("Select 2 or more PDF files, then merge.")

        Button(onClick = { picker.launch(arrayOf("application/pdf")) }, enabled = !isMerging) {
            Text("Pick PDFs")
        }

        Text("Selected: ${selectedUris.size}")

        Button(onClick = { viewModel.startMerge() }, enabled = !isMerging && selectedUris.size >= 2) {
            Text(if (isMerging) "Merging..." else "Merge")
        }

        if (isMerging) {
            LinearProgressIndicator(progress = progress / 100f)
            Text("Progress: $progress%")
        }

        error?.let { Text("Error: $it") }
    }
}
