package com.awesomepdf.presentation.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomepdf.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun AuthScreen(onContinue: () -> Unit, viewModel: AuthViewModel = hiltViewModel()) {
    val status by viewModel.status.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).result
            val token = account?.idToken
            if (!token.isNullOrBlank()) {
                viewModel.signInWithGoogle(token, onContinue)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign in")
        Text(status)
        Button(
            onClick = {
                val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                    .build()
                val client = GoogleSignIn.getClient(context, options)
                launcher.launch(client.signInIntent)
            },
            modifier = Modifier.padding(top = 16.dp),
            enabled = activity != null
        ) {
            Text("Sign in with Google")
        }
        Button(onClick = { viewModel.skip(onContinue) }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Skip")
        }
    }
}
