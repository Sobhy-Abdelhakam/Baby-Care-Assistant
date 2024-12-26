package dev.sobhy.babycareassistant.alarm.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.babycareassistant.NotificationActivity
import dev.sobhy.babycareassistant.R
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.notification.data.repository.NotificationRepository
import dev.sobhy.babycareassistant.notification.domain.Notification
import dev.sobhy.babycareassistant.utils.DEFAULT_FEEDING_MESSAGE
import dev.sobhy.babycareassistant.utils.DEFAULT_FEEDING_TITLE
import dev.sobhy.babycareassistant.utils.DEFAULT_NOTIFICATION_TITLE
import dev.sobhy.babycareassistant.utils.DIAPER_REMINDER_TITLE
import dev.sobhy.babycareassistant.utils.NOTIFICATION_CHANNEL_ID
import dev.sobhy.babycareassistant.utils.TYPE_DIAPER
import dev.sobhy.babycareassistant.utils.TYPE_FEEDING
import dev.sobhy.babycareassistant.utils.TYPE_VACCINATION
import dev.sobhy.babycareassistant.utils.VACCINATION_REMINDER_TITLE
import dev.sobhy.babycareassistant.utils.calculateBabyAgeInMonths
import dev.sobhy.babycareassistant.utils.getFeedingDataForAge
import dev.sobhy.babycareassistant.utils.scheduleNextNotification
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationRepository: NotificationRepository
    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra("type") ?: return
        Log.d(TAG, "onReceive: $type")
        when (type) {
            TYPE_VACCINATION -> handleVaccinationNotification(context, intent)
            TYPE_FEEDING -> handleFeedingNotification(context, intent)
            TYPE_DIAPER -> handleDiaperNotification(context, intent)
            else -> Log.w(TAG, "Unknown notification type: $type")
        }
    }

    private fun handleFeedingNotification(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: DEFAULT_FEEDING_TITLE
        val message = intent.getStringExtra("message") ?: DEFAULT_FEEDING_MESSAGE

        showNotification(context, title, message)
        storeNotificationInFirebase(title, message)
        // Fetch baby's age from Firebase and determine next feeding schedule
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: return)
            .get()
            .addOnSuccessListener { document ->
                val birthDate = document.getString("birthDate") ?: return@addOnSuccessListener
                val babyAgeInMonths = calculateBabyAgeInMonths(birthDate)

                getFeedingDataForAge(context, babyAgeInMonths)?.notes?.let { notes ->
                    scheduleNextNotification(context, notes)
                }
            }
    }

    private fun handleDiaperNotification(context: Context, intent: Intent) {
        val diaper = intent.getParcelableExtra<Diapers>("data") ?: return
        val timeIndex = intent.getIntExtra("timeIndex", 0)

        val pendingIntent = notificationIntent(context) {
            putExtra("notificationData", diaper)
            putExtra("timeIndex", timeIndex)
        }
        showNotification(
            context,
            DIAPER_REMINDER_TITLE,
            "It's time for diaper change number: ${timeIndex + 1}",
            pendingIntent,
        )
        storeNotificationInFirebase(
            DIAPER_REMINDER_TITLE,
            "time ${timeIndex + 1}: ${diaper.timesOfDiapersChange[timeIndex]}"
        )
    }

    private fun handleVaccinationNotification(context: Context, intent: Intent) {
        val vaccination = intent.getParcelableExtra<Vaccination>("data") ?: return

        val pendingIntent = notificationIntent(context){
            putExtra("notificationData", vaccination)
        }
        showNotification(
            context,
            VACCINATION_REMINDER_TITLE,
            "It's time for vaccination: ${vaccination.name}",
            pendingIntent,
        )
        storeNotificationInFirebase(
            VACCINATION_REMINDER_TITLE,
            "${vaccination.name} ${vaccination.code}"
        )
    }

    private fun storeNotificationInFirebase(title: String, message: String) {
        val notification = Notification(
            title = title,
            message = message,
            date = System.currentTimeMillis()
        )
        CoroutineScope(Dispatchers.IO).launch {
            notificationRepository.saveNotification(notification)
        }
    }
    private fun notificationIntent(context: Context, putExtras: Intent.() -> Unit): PendingIntent {
        val notificationIntent = Intent(context, NotificationActivity::class.java)
            .apply(putExtras)
        return PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showNotification(
        context: Context,
        title: String = DEFAULT_NOTIFICATION_TITLE,
        content: String,
        pendingIntent: PendingIntent? = null,
    ) {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(content.hashCode(), notification)
    }

    companion object {
        private const val TAG = "NotificationReceiver"
    }
}