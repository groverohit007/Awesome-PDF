package com.awesomepdf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.awesomepdf.presentation.navigation.AppNavHost
import com.awesomepdf.ui.theme.AwesomePdfTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AwesomePdfTheme {
                AppNavHost()
            }
        }
    }
}
