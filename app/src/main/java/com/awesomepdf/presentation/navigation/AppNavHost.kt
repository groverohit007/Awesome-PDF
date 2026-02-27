package com.awesomepdf.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.awesomepdf.presentation.auth.AuthScreen
import com.awesomepdf.presentation.main.ai.AiScreen
import com.awesomepdf.presentation.main.files.FilesScreen
import com.awesomepdf.presentation.main.home.HomeScreen
import com.awesomepdf.presentation.main.settings.SettingsScreen
import com.awesomepdf.presentation.main.tools.ToolsScreen
import com.awesomepdf.presentation.onboarding.OnboardingScreen
import com.awesomepdf.presentation.paywall.PaywallScreen
import com.awesomepdf.presentation.splash.SplashScreen

@Composable
fun AppNavHost(appStateViewModel: AppStateViewModel) {
    val navController = rememberNavController()
    val tabs = listOf("home", "tools", "files", "ai", "settings")
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen { navController.navigate("onboarding") { popUpTo("splash") { inclusive = true } } }
        composable("onboarding") {
            OnboardingScreen {
                appStateViewModel.completeOnboarding()
                navController.navigate("auth")
            }
        }
        composable("auth") { AuthScreen(onContinue = { navController.navigate("main") { popUpTo("auth") { inclusive = true } } }) }
        composable("paywall") { PaywallScreen(onBack = { navController.popBackStack() }) }
        composable("main") {
            Scaffold(
                bottomBar = {
                    val current by navController.currentBackStackEntryAsState()
                    if (current?.destination?.route in tabs) {
                        NavigationBar {
                            val items = listOf(
                                "home" to Icons.Default.Home,
                                "tools" to Icons.Default.Build,
                                "files" to Icons.Default.Folder,
                                "ai" to Icons.Default.Psychology,
                                "settings" to Icons.Default.Settings
                            )
                            items.forEach { (route, icon) ->
                                NavigationBarItem(
                                    selected = current?.destination?.route == route,
                                    onClick = {
                                        navController.navigate(route) {
                                            popUpTo(navController.graph.findStartDestination().id)
                                            launchSingleTop = true
                                        }
                                    },
                                    icon = { Icon(icon, null) },
                                    label = { Text(route.replaceFirstChar { it.uppercase() }) }
                                )
                            }
                        }
                    }
                }
            ) { padding ->
                NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(padding)) {
                    composable("home") { HomeScreen(onOpenPaywall = { navController.navigate("paywall") }) }
                    composable("tools") { ToolsScreen() }
                    composable("files") { FilesScreen() }
                    composable("ai") { AiScreen() }
                    composable("settings") { SettingsScreen() }
                }
            }
        }
    }
}
