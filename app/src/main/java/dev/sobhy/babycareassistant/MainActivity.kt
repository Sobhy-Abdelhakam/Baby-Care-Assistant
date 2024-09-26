package dev.sobhy.babycareassistant

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.babycareassistant.navigation.NavigationGraph
import dev.sobhy.babycareassistant.navigation.ScreenRoutes
import dev.sobhy.babycareassistant.ui.theme.BabyCareAssistantTheme

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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        enableEdgeToEdge()

        setContent {
            BabyCareAssistantTheme {
                val navController = rememberNavController()
                SetupNavigation(navController = navController)
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

    // Set up navigation based on authentication state
    @Composable
    private fun SetupNavigation(navController: NavHostController) {
        NavigationGraph(
            navController = navController,
            startDestination = if (auth.currentUser != null) ScreenRoutes.Home.route else ScreenRoutes.Authentication.route
        )
    }
}