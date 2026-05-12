package com.mindmatrix.nammakathey.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindmatrix.nammakathey.Hero

@Composable
fun MainScreen(
    heroes: List<Hero>,
    onNavigateToStory: (Hero) -> Unit,
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Quiz", "Profile")
    val icons = listOf(Icons.Default.Home, Icons.Default.List, Icons.Default.Person)
    val routes = listOf("home", "quiz", "profile")

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            bottomNavController.navigate(routes[index]) {
                                popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                MapScreen(
                    heroes = heroes,
                    onNavigateToStory = onNavigateToStory,
                    onNavigateToProfile = {
                        selectedItem = 2
                        bottomNavController.navigate("profile") {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable("quiz") {
                QuizListScreen(
                    heroes = heroes,
                    onStartQuiz = { hero ->
                        bottomNavController.navigate("active_quiz/${hero.id}")
                    }
                )
            }
            composable("profile") {
                ProfileScreen(
                    onLogout = onLogout
                )
            }
            composable("active_quiz/{heroId}") { backStackEntry ->
                val heroId = backStackEntry.arguments?.getString("heroId")
                val hero = heroes.find { it.id == heroId }
                if (hero != null) {
                    ActiveQuizScreen(
                        hero = hero,
                        onQuizFinished = {
                            bottomNavController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
