package com.awesomepdf.presentation.main.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
fun AiScreen(viewModel: AiViewModel = hiltViewModel()) {
    val response by viewModel.response.collectAsState()
    val quota by viewModel.quota.collectAsState()
    val input = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("AI Assistant")
        Text("Page quota remaining: $quota")
        OutlinedTextField(value = input.value, onValueChange = { input.value = it }, label = { Text("Paste PDF text") })
        Button(onClick = { viewModel.summarize(input.value) }) { Text("Summarize") }
        Text(response)
    }
}
