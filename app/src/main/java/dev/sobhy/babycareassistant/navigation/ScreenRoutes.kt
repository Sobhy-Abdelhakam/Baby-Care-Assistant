package dev.sobhy.babycareassistant.navigation

sealed class ScreenRoutes(val route: String) {
    data object Authentication : ScreenRoutes("/authentication")
    data object Home: ScreenRoutes("/home")
    data object Profile: ScreenRoutes("/home/profile")
    data object Notification: ScreenRoutes("/home/notification")
    data object VaccinationScreen: ScreenRoutes("/home/vaccination")
    data object AddVaccination: ScreenRoutes("/home/vaccination/addVaccination")
    data object Breastfeed: ScreenRoutes("/home/breastfeed")
    data object AddBreastfeed: ScreenRoutes("/home/breastfeed/addBreastfeed")
    data object BabyGrowth: ScreenRoutes("/home/babyGrowth")
    data object AddBabyGrowth: ScreenRoutes("/home/babyGrowth/addBabyGrowth")
    data object BabySleep: ScreenRoutes("/home/babySleep")
    data object AddBabySleep: ScreenRoutes("/home/babySleep/addBabySleep")
    data object Diapers: ScreenRoutes("/home/diaper")
    data object AddDiaper: ScreenRoutes("/home/diaper/addDiaper")
    data object HealthInformation: ScreenRoutes("/home/healthInformation")
    data object AddHealthInformation: ScreenRoutes("/home/healthInformation/addHealthInformation")
}
