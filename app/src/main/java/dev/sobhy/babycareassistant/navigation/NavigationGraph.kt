package dev.sobhy.babycareassistant.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.sobhy.babycareassistant.breastfeeding.BreastFeedingScreen
import dev.sobhy.babycareassistant.diapers.DiapersScreen
import dev.sobhy.babycareassistant.diapers.add.AddDiaperScreen
import dev.sobhy.babycareassistant.growth.BabyGrowthScreen
import dev.sobhy.babycareassistant.growth.add.AddGrowthScreen
import dev.sobhy.babycareassistant.healthinfo.HealthInfoScreen
import dev.sobhy.babycareassistant.healthinfo.addhealthinfo.AddHealthInfoScreen
import dev.sobhy.babycareassistant.home.HomeScreen
import dev.sobhy.babycareassistant.notification.NotificationScreen
import dev.sobhy.babycareassistant.profile.ProfileScreen
import dev.sobhy.babycareassistant.sleep.BabySleepScreen
import dev.sobhy.babycareassistant.sleep.add.AddSleepScreen
import dev.sobhy.babycareassistant.vaccination.VaccinationScreen
import dev.sobhy.babycareassistant.vaccination.addvaccination.AddVaccinationScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(navController = navController, startDestination = startDestination){
        authGraph(navController)

        composable(ScreenRoutes.Home.route){
            HomeScreen(navController = navController)
        }
        composable(ScreenRoutes.VaccinationScreen.route){
            VaccinationScreen(navController = navController)
        }
        composable(ScreenRoutes.AddVaccination.route){
            AddVaccinationScreen(navController = navController)
        }
        composable(ScreenRoutes.Breastfeed.route){
            BreastFeedingScreen(navController)
        }
//        composable(
//            route = ScreenRoutes.AddBreastfeed.route + "/{feedingId}",
//            arguments = listOf(
//                navArgument("feedingId"){
//                    type = NavType.StringType
//                    nullable = true
//                    defaultValue = null
//                }
//            )
//        ){
//            val feedingId = it.arguments?.getString("feedingId")
//            AddFeedScreen(navController, feedingId = feedingId)
//        }
        composable(ScreenRoutes.BabyGrowth.route){
            BabyGrowthScreen(navController)
        }
        composable(
            route = ScreenRoutes.AddBabyGrowth.route + "/{growthId}",
            arguments = listOf(
                navArgument("growthId"){
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ){
            val growthId = it.arguments?.getString("growthId")
            AddGrowthScreen(navController, growthId = growthId)
        }
        composable(ScreenRoutes.BabySleep.route){
            BabySleepScreen(navController)
        }
        composable(
            route = ScreenRoutes.AddBabySleep.route + "/{sleepId}",
            arguments = listOf(
                navArgument("sleepId"){
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ){
            val sleepId = it.arguments?.getString("sleepId")
            AddSleepScreen(navController, sleepId = sleepId)
        }
        composable(ScreenRoutes.Diapers.route){
            DiapersScreen(navController)
        }
        composable(
            route = ScreenRoutes.AddDiaper.route + "/{diaperId}",
            arguments = listOf(
                navArgument("diaperId"){
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ){
            val diaperId = it.arguments?.getString("diaperId")
            AddDiaperScreen(navController, diaperId = diaperId)
        }
        composable(ScreenRoutes.HealthInformation.route){
            HealthInfoScreen(navController)
        }
        composable(
            route = ScreenRoutes.AddHealthInformation.route +"/{healthInfoId}",
            arguments = listOf(
                navArgument("healthInfoId"){
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ){
            val healthInfoId = it.arguments?.getString("healthInfoId")
            AddHealthInfoScreen(navController, healthInfoId = healthInfoId)
        }
        composable(ScreenRoutes.Profile.route) {
            ProfileScreen(navController)
        }
        composable(ScreenRoutes.Notification.route){
            NotificationScreen(navController)
        }
    }
}