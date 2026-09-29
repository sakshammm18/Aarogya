package com.example.aarogya.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChartOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.aarogya.composables.CommunityAndLeaderboardScreen
import com.example.aarogya.composables.LoginScreen
import com.example.aarogya.composables.MacrosAnalysisScreen
import com.example.aarogya.composables.SplashScreen
import com.example.aarogya.composables.UserInfoScreen
import com.example.aarogya.dashboard.WearableDashboard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AarogyaNavigation(context: Context) {
    val navController = rememberNavController()

    // 🟩 State to hold user data globally
    var username by remember { mutableStateOf("") }
    var age by remember { mutableStateOf(0) }
    var gender by remember { mutableStateOf("") }
    var height by remember { mutableStateOf(0f) }
    var weight by remember { mutableStateOf(0f) }

    Scaffold(
        topBar = {
            val currentRoute = currentRoute(navController)
            if (currentRoute !in listOf("splash", "login","info")) {
                TopAppBar(
                    title = { Text("Aarogya", color = Color.Black) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CE116))
                )
            }
        },
        bottomBar = {
            val currentRoute = currentRoute(navController)
            if (currentRoute in listOf("dashboard", "community", "macro")) {
                BottomAppBar(containerColor = Color(0xFF4CE116)) {
                    val items = listOf(
                        BottomNavItem("dashboard", Icons.Default.Home, "Home"),
                        BottomNavItem("community", Icons.Default.Group, "Community"),
                        BottomNavItem("macro", Icons.Default.PieChartOutline, "Macro")
                    )

                    items.forEach { item ->
                        IconButton(
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo("dashboard") { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.description,
                                tint = if (currentRoute == item.route) Color.White else Color.Black
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("login") { LoginScreen(navController) }

            // 🟢 Info screen collects and updates data
            composable("info") {
                UserInfoScreen(

                    onSubmit = { name, a, g, h, w, activity, goal ->
                        if (name.isBlank() || a.isBlank() || g.isBlank() || h.isBlank() || w.isBlank()) {
                            // Show a snackbar instead of navigating
                            // You can pass a callback to UserInfoScreen to show an error message
                            navController.currentBackStackEntry?.savedStateHandle?.set("errorMessage", "All fields are required")
                            return@UserInfoScreen
                        }
                        // Safely parse and handle blank inputs
                        val safeAge = a.toIntOrNull() ?: 0
                        val safeHeight = h.toFloatOrNull() ?: 0f
                        val safeWeight = w.toFloatOrNull() ?: 0f
                        val safeGender = g.ifBlank { "Unknown" }
                        if (safeAge == null || safeHeight == null || safeWeight == null) {
                            navController.currentBackStackEntry?.savedStateHandle?.set("errorMessage", "Please enter valid numbers for age, height, and weight")
                            return@UserInfoScreen
                        }

                        username = name
                        age = safeAge
                        gender = safeGender
                        height = safeHeight
                        weight = safeWeight

                        navController.navigate("dashboard")

                        // 🟣 Also store activity + goal in SavedStateHandle for macro screen
                        navController.currentBackStackEntry?.savedStateHandle?.set("activityLevel", activity)
                        navController.currentBackStackEntry?.savedStateHandle?.set("goal", goal)
                    }
                )
            }

            // 🟢 Dashboard uses stored data
            composable("dashboard") {
                WearableDashboard(
                    navController = navController,
                    username = username,
                    age = age,
                    gender = gender,
                    height = height,
                    weight = weight
                )
            }

            composable("community") { CommunityAndLeaderboardScreen(navController) }
            composable("macro") {
                val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
                val activityLevel = savedStateHandle?.get<String>("activityLevel") ?: "moderate"
                val goal = savedStateHandle?.get<String>("goal") ?: "maintain"

                MacrosAnalysisScreen(
                    navController = navController,
                    age = age,
                    gender = gender,
                    heightCm = height.toDouble(),
                    weightKg = weight.toDouble(),
                    activityLevel = activityLevel,
                    goal = goal
                )
            }

        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

data class BottomNavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val description: String
)
