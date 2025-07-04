package com.example.shared_preferences

import DataShow
import TaskScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screens.TaskScreen.route) {
        composable(Screens.TaskScreen.route) { TaskScreen(navController) }

        composable(
            route = "dataShowScreen/{username}/{email}/{password}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username")
            val email = backStackEntry.arguments?.getString("email")
            val password = backStackEntry.arguments?.getString("password")

            DataShowScreen(
                navController = navController,
                username = username,
                email = email,
                password = password
            )
        }
        composable(
            route = "DataShow/{username}/{email}/{password}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username")
            val email = backStackEntry.arguments?.getString("email")
            val password = backStackEntry.arguments?.getString("password")

            DataShow(
                navController,
                username,
                email,
                password
            )
        }

    }


}

sealed class Screens(val route: String) {
    object SharedPreferences : Screens("SharedPreferences")
    object DataShowScreen : Screens("DataShowScreen")
    object TaskScreen : Screens("TaskScreen")
    object DataShow : Screens("DataShow")
}
