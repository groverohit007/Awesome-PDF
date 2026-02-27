package com.awesomepdf.presentation.navigation

import android.net.Uri
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.awesomepdf.presentation.main.ai.AiScreen
import com.awesomepdf.presentation.main.files.FilesScreen
import com.awesomepdf.presentation.main.home.HomeScreen
import com.awesomepdf.presentation.main.settings.SettingsScreen
import com.awesomepdf.presentation.main.tools.MergeResultScreen
import com.awesomepdf.presentation.main.tools.ToolsScreen
import com.awesomepdf.presentation.onboarding.OnboardingScreen
import com.awesomepdf.presentation.splash.SplashScreen

private data class BottomNavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val bottomNavItems = listOf(
    BottomNavItem("home", "Home", Icons.Default.Home),
    BottomNavItem("tools", "Tools", Icons.Default.Build),
    BottomNavItem("files", "Files", Icons.Default.Folder),
    BottomNavItem("ai", "AI", Icons.Default.Psychology),
    BottomNavItem("settings", "Settings", Icons.Default.Settings)
)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isMainDestination = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (isMainDestination) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("splash") {
                SplashScreen(onDone = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("onboarding") {
                OnboardingScreen(onContinue = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                })
            }
            composable("home") { HomeScreen() }
            composable("tools") {
                ToolsScreen(onMergeCompleted = { outputPath ->
                    navController.navigate("merge_result/${Uri.encode(outputPath)}")
                })
            }
            composable("files") { FilesScreen() }
            composable("ai") { AiScreen() }
            composable("settings") { SettingsScreen() }
            composable(
                route = "merge_result/{outputPath}",
                arguments = listOf(navArgument("outputPath") { type = NavType.StringType })
            ) { backStack ->
                val outputPath = Uri.decode(backStack.arguments?.getString("outputPath").orEmpty())
                MergeResultScreen(
                    outputPath = outputPath,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
