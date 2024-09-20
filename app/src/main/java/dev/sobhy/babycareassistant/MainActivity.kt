package dev.sobhy.babycareassistant

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.babycareassistant.breastfeeding.add.AddFeedingViewModel
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.navigation.NavigationGraph
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.CardFromNotification
import dev.sobhy.babycareassistant.ui.theme.BabyCareAssistantTheme
import dev.sobhy.babycareassistant.vaccination.data.Vaccination

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val update: AddFeedingViewModel = hiltViewModel()
            BabyCareAssistantTheme {
                val navController = rememberNavController()
                var showDialog by remember { mutableStateOf(false) }
                NavigationGraph(
                    navController = navController,
                    startDestination = if (auth.currentUser != null) ScreenRoutes.Home.route else ScreenRoutes.Authentication.route
                )

                val feedingData = intent?.getParcelableExtra<BreastFeed>("feedingData")
                val diapersData = intent?.getParcelableExtra<Diapers>("diaperData")
                val vaccinationData = intent?.getParcelableExtra<Vaccination>("vaccinationData")
                val notificationData = feedingData ?: diapersData ?: vaccinationData
                val title =
                    if (feedingData != null) "Breastfeeding Data" else if (diapersData != null) "Diapers Data" else vaccinationData?.name
                LaunchedEffect(notificationData) {
                    if (notificationData != null) {
                        showDialog = true
                    }
                }
                if (showDialog) {
                    notificationData?.let {
                        CardFromNotification(
                            title = title ?: "",
                            dateAndDay = when (it) {
                                is BreastFeed -> "${it.date}, ${it.day}"
                                is Diapers -> "${it.date}, ${it.day}"
                                else -> ""
                            },
                            timesPerDay = when (it) {
                                is BreastFeed -> it.numberOfFeedingsPerDay.toString()
                                is Diapers -> it.numberOfDiapersChange.toString()
                                else -> ""
                            },
                            amountOfMilk = null,
                            timesOfTimes = when (it) {
                                is BreastFeed -> it.timeOfTimes.map { it.time }
                                is Diapers -> it.timesOfDiapersChange
                                else -> emptyList()
                            },
                            dismiss = { showDialog = false },
                            onClick = {
                                update.addOrUpdateFeeding(feedingData?.id)
                            }
                        )
                    }
                }
            }
        }
    }
}