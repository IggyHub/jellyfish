package com.example.myapplication4

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myapplication4.data.AppDatabase
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun MyApp(database: AppDatabase) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Application") },
                navigationIcon = {
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    if (backStackEntry?.destination?.route != "home") {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", Modifier.padding(innerPadding)) {
            composable("home") { HomeScreen(database) }
            composable("dashboard") { DashboardScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        BottomNavigationItem(
            icon = { /* Add Icons here if needed */ },
            label = { Text("Home") },
            selected = currentDestination?.route == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        BottomNavigationItem(
            icon = { /* Add Icons here if needed */ },
            label = { Text("Dashboard") },
            selected = currentDestination?.route == "dashboard",
            onClick = {
                navController.navigate("dashboard") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        BottomNavigationItem(
            icon = { /* Add Icons here if needed */ },
            label = { Text("Settings") },
            selected = currentDestination?.route == "settings",
            onClick = {
                navController.navigate("settings") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
    }
}
