package dev.sobhy.babycareassistant

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.diapers.data.repository.DiapersRepository
import dev.sobhy.babycareassistant.navigation.NavigationGraph
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.composable.DiapersCardFromNotification
import dev.sobhy.babycareassistant.ui.composable.FeedingCardFromNotification
import dev.sobhy.babycareassistant.ui.composable.VaccinationCardFromNotification
import dev.sobhy.babycareassistant.ui.theme.BabyCareAssistantTheme
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import dev.sobhy.babycareassistant.vaccination.data.repository.VaccinationRepository
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Notification permission launcher for Android 13+
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d("MainActivity", "Notification permission granted")
                // If needed, you can start the notification process here
            } else {
                Log.d("MainActivity", "Notification permission denied")
                showSettingsDialog() // Show a dialog guiding the user to settings
            }
        }
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var notificationData: Parcelable? = null
    private var timeIndex: Int? = null

    @Inject
    lateinit var vaccinationRepository: VaccinationRepository

    @Inject
    lateinit var breastFeedRepository: FeedingRepository

    @Inject
    lateinit var diapersRepository: DiapersRepository

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        enableEdgeToEdge()
        handleNotificationData()

        setContent {
            BabyCareAssistantTheme {
                val navController = rememberNavController()
                var showDialog by remember { mutableStateOf(notificationData != null) }

                Box(modifier = Modifier.fillMaxSize()) {
                    // Set up navigation based on authentication state
                    SetupNavigation(navController = navController)

                    if (showDialog) {
                        notificationData?.let { data ->
                            when (data) {
                                is Vaccination -> VaccinationCardFromNotification(
                                    vaccination = data, modifier = Modifier.align(
                                        Alignment.Center
                                    )
                                ) {
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
                                    modifier = Modifier.align(Alignment.Center),
                                    later = { showDialog = false }) {
                                    breastFeedRepository.updateFeedingTimeFromNotification(
                                        docId = data.id,
                                        index = timeIndex!!,
                                        newTime = LocalTime.now().toString()
                                    )
                                    showDialog = false
                                }

                                is Diapers -> DiapersCardFromNotification(
                                    diapers = data,
                                    indexOfColorChange = timeIndex!!,
                                    modifier = Modifier.align(Alignment.Center),
                                    later = { showDialog = false }) {
                                    diapersRepository.updateDiaperChangeTimeFromNotification(
                                        diapersId = data.id,
                                        index = timeIndex!!,
                                        newTime = LocalTime.now().toString()
                                    )
                                    showDialog = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Request the notification permission for Android 13+
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(permission)
        }
    }

    // Show a dialog that guides the user to the app's settings if the permission is denied
    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Notification Permission Required")
            .setMessage("Please enable notification permission in settings to receive alerts.")
            .setPositiveButton("Settings") { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun handleNotificationData() {
        notificationData = intent?.getParcelableExtra("notificationData")
        timeIndex = intent?.getIntExtra("timeIndex", -1)?.takeIf { it >= 0 }
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