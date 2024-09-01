package dev.sobhy.babycareassistant.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import dev.sobhy.babycareassistant.authentication.persentation.login.LoginScreen
import dev.sobhy.babycareassistant.authentication.persentation.register.RegisterScreen

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(startDestination = AuthenticationRoutes.LOGIN.route, route = ScreenRoutes.Authentication.route) {
        composable(route = AuthenticationRoutes.LOGIN.route) {
            LoginScreen(navController = navController)
        }
        composable(route = AuthenticationRoutes.REGISTER.route) {
            RegisterScreen(navController)
        }

    }
}