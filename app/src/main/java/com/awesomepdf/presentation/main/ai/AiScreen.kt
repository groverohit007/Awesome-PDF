package com.awesomepdf.presentation.main.ai

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomepdf.presentation.common.FeatureGate
import java.io.OutputStreamWriter

@Composable
fun AiScreen(onOpenPaywall: () -> Unit, viewModel: AiViewModel = hiltViewModel()) {
    val summary by viewModel.summary.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val chat by viewModel.chat.collectAsState()
    val citations by viewModel.citations.collectAsState()
    val usage by viewModel.usage.collectAsState()

    val extractedText = remember { mutableStateOf("") }
    val question = remember { mutableStateOf("") }
    val context = LocalContext.current

    val pdfPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            extractedText.value = "[Placeholder extraction] Parsed text from ${it.lastPathSegment ?: "selected.pdf"}"
        }
    }

    val exportMarkdown = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/markdown")) { uri ->
        if (uri != null) {
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                OutputStreamWriter(stream).use { writer ->
                    writer.write(viewModel.exportNotesMarkdown())
                }
            }
        }
    }

    FeatureGate(
        lockedMessage = "AI tools are Premium. Upgrade to continue.",
        onOpenPaywall = onOpenPaywall
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("AI Workspace")
            Text("Monthly usage: ${usage.used}/${usage.monthlyLimit} • Remaining: ${usage.remaining}")

            Button(onClick = { pdfPicker.launch(arrayOf("application/pdf")) }) {
                Text("Select PDF")
            }
            Text("Extracted text placeholder: ${if (extractedText.value.isBlank()) "None" else "Ready"}")

            Button(onClick = { viewModel.summarizeExtractedText(extractedText.value.ifBlank { "No text extracted yet" }) }) {
                Text("Summarise PDF")
            }
            Text(summary)

            Button(onClick = { viewModel.makeNotes(extractedText.value.ifBlank { "No text extracted yet" }) }) {
                Text("Make Notes")
            }
            if (notes.isNotEmpty()) {
                notes.forEach { Text("• $it") }
                Button(onClick = { exportMarkdown.launch("awesome_pdf_notes.md") }) {
                    Text("Export to Markdown")
                }
            }

            OutlinedTextField(
                value = question.value,
                onValueChange = { question.value = it },
                label = { Text("Ask PDF") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.askPdf(question.value) }) { Text("Send") }
            }
            chat.forEach { msg -> Text("${msg.role}: ${msg.content}") }
            if (citations.isNotEmpty()) {
                Text("Citations (placeholder): ${citations.joinToString()}")
            }
        }
    }
}
