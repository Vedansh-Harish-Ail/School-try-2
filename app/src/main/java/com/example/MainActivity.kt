package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.data.StudentPortalViewModel
import com.example.data.StudentPortalViewModelFactory
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Resolve view model dependencies
                val app = application as StudentPortalApplication
                val viewModel: StudentPortalViewModel = viewModel(
                    factory = StudentPortalViewModelFactory(app, app.repository)
                )

                val isLoggedIn by viewModel.isLoggedIn.collectAsState()

                if (!isLoggedIn) {
                    var loginSubRoute by remember { mutableStateOf("login") }
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        if (loginSubRoute == "login") {
                            LoginScreen(
                                viewModel = viewModel,
                                onNavigateToForgotPassword = { loginSubRoute = "forgot_password" }
                            )
                        } else {
                            ForgotPasswordScreen(
                                viewModel = viewModel,
                                onNavigateBack = { loginSubRoute = "login" }
                            )
                        }
                    }
                } else {
                    AppNavigationShell(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigationShell(viewModel: StudentPortalViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom navigation bar for sub-screens in deep hierarchies
    val bottomTabs = listOf("dashboard", "attendance", "marks", "notices_feed", "profile")
    val shouldShowBottomBar = currentRoute in bottomTabs

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomBar) {
                Column {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline,
                        thickness = 1.dp
                    )
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        tonalElevation = 0.dp,
                        modifier = Modifier.testTag("app_bottom_bar")
                    ) {
                    // Item 1: Home Dashboard
                    NavigationBarItem(
                        selected = currentRoute == "dashboard",
                        onClick = {
                            navController.navigate("dashboard") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home", style = MaterialTheme.typography.labelSmall) }
                    )

                    // Item 2: Attendance
                    NavigationBarItem(
                        selected = currentRoute == "attendance" || currentRoute == "absence_details",
                        onClick = {
                            navController.navigate("attendance") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Attendance") },
                        label = { Text("Attendance", style = MaterialTheme.typography.labelSmall) }
                    )

                    // Item 3: Marks
                    NavigationBarItem(
                        selected = currentRoute == "marks",
                        onClick = {
                            navController.navigate("marks") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Analytics, contentDescription = "Marks") },
                        label = { Text("Marks") }
                    )

                    // Item 4: Notices
                    NavigationBarItem(
                        selected = currentRoute == "notices_feed",
                        onClick = {
                            navController.navigate("notices_feed") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Campaign, contentDescription = "Notices") },
                        label = { Text("Notices") }
                    )

                    // Item 5: Profile
                    NavigationBarItem(
                        selected = currentRoute == "profile" || currentRoute == "personal_info",
                        onClick = {
                            navController.navigate("profile") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") }
                    )
                }
            }
        }
    },
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Dashboard (Home)
            composable("dashboard") {
                StudentDashboardScreen(
                    viewModel = viewModel,
                    onNavigateToAttendance = { navController.navigate("attendance") },
                    onNavigateToMarks = { navController.navigate("marks") },
                    onNavigateToNotices = { navController.navigate("notices_feed") },
                    onNavigateToProfile = { navController.navigate("profile") }
                )
            }

            // Attendance
            composable("attendance") {
                AttendanceScreen(
                    viewModel = viewModel,
                    onNavigateToAbsenceDetails = { navController.navigate("absence_details") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Absence Details Log
            composable("absence_details") {
                AbsenceDetailsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Performance Marks Review
            composable("marks") {
                MarksScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Notices Screen
            composable("notices_feed") {
                NoticesScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Profile Screen
            composable("profile") {
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToPersonalInfo = { navController.navigate("personal_info") },
                    onLogout = { viewModel.logout() }
                )
            }

            // Personal Info
            composable("personal_info") {
                PersonalInfoScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
