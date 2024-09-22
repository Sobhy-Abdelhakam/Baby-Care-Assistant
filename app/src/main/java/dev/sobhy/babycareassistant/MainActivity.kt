package dev.sobhy.babycareassistant

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.navigation.NavigationGraph
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.DiapersCardFromNotification
import dev.sobhy.babycareassistant.ui.composable.FeedingCardFromNotification
import dev.sobhy.babycareassistant.ui.composable.VaccinationCardFromNotification
import dev.sobhy.babycareassistant.ui.theme.BabyCareAssistantTheme
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import dev.sobhy.babycareassistant.vaccination.data.repository.VaccinationRepository
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var notificationData: Parcelable? = null
    private var timeIndex: Int? = null

    @Inject
    lateinit var vaccinationRepository: VaccinationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        notificationData = intent?.getParcelableExtra("notificationData")
        timeIndex = intent?.getIntExtra("timeIndex", -1)

        setContent {
            BabyCareAssistantTheme {
                val navController = rememberNavController()
                var showDialog by remember { mutableStateOf(false) }

                LaunchedEffect(notificationData) {
                    if (notificationData != null) {
                        showDialog = true
                    }
                }
                // Set up navigation based on authentication state
                SetupNavigation(navController = navController)

                if (showDialog) {
                    notificationData?.let { data ->
                        when (data) {
                            is Vaccination -> VaccinationCardFromNotification(vaccination = data) {
                                vaccinationRepository.updateVaccinationStatus(
                                    data.id,
                                    onSuccess = {
                                        Log.d(
                                            "MainActivity",
                                            "Vaccination status updated successfully"
                                        )
                                        showDialog = false
                                    },
                                    onError = { exception ->
                                        Log.e(
                                            "MainActivity",
                                            "Error updating vaccination status",
                                            exception
                                        )
                                    }
                                )
                            }

                            is BreastFeed -> FeedingCardFromNotification(
                                breastFeed = data,
                                indexOfColorChange = timeIndex!!,
                                later = { showDialog = false }) {
                                showDialog = false
                            }

                            is Diapers -> DiapersCardFromNotification(
                                diapers = data,
                                indexOfColorChange = timeIndex!!,
                                later = { showDialog = false }) {
                                showDialog = false
                            }
                        }
                    }
                }
            }
        }
    }

    // Set up navigation based on authentication state
    @Composable
    private fun SetupNavigation(navController: NavHostController) {
        NavigationGraph(
            navController = navController,
            startDestination = if (auth.currentUser != null) ScreenRoutes.Home.route else ScreenRoutes.Authentication.route
        )
    }
}