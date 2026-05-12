package com.mindmatrix.nammakathey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.mindmatrix.nammakathey.data.SessionManager
import com.mindmatrix.nammakathey.ui.screens.*
import com.mindmatrix.nammakathey.ui.theme.NammaKatheyTheme
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val heroes = loadHeroes()

        setContent {
            NammaKatheyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(heroes = heroes)
                }
            }
        }
    }

    private fun loadHeroes(): List<Hero> {
        try {
            val inputStream = assets.open("heroes.json")
            val reader = InputStreamReader(inputStream)
            val heroData = Gson().fromJson(reader, HeroData::class.java)
            return heroData.heroes
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
}

@Composable
fun AppNavigation(heroes: List<Hero>) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    // Start with splash, then decide where to go
    val startDest = "splash"

    NavHost(
        navController = navController,
        startDestination = startDest,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)) }
    ) {
        composable("splash") {
            SplashScreen(
                isLoggedIn = sessionManager.isLoggedIn(),
                onNavigateToMain = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegistrationScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainScreen(
                heroes = heroes,
                onNavigateToStory = { hero ->
                    navController.navigate("story/${hero.id}")
                },
                onLogout = {
                    sessionManager.logoutUser()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "story/{heroId}",
            arguments = listOf(navArgument("heroId") { type = NavType.StringType })
        ) { backStackEntry ->
            val heroId = backStackEntry.arguments?.getString("heroId")
            val hero = heroes.find { it.id == heroId }
            if (hero != null) {
                StoryScreen(
                    hero = hero,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
