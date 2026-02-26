package com.awesomepdf.presentation.auth

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

@Composable
fun AuthScreen(onContinue: () -> Unit, viewModel: AuthViewModel = hiltViewModel()) {
    val status by viewModel.status.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign in")
        Text(status)
        Button(onClick = { viewModel.signInWithGoogleStub(onContinue) }, modifier = Modifier.padding(top = 16.dp)) {
            Text("Sign in with Google")
        }
        Button(onClick = { viewModel.skip(onContinue) }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Skip")
        }
    }
}
