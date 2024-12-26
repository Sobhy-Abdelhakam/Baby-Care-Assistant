package dev.sobhy.babycareassistant

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.babycareassistant.alarm.data.FeedingSchedule
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.diapers.data.repository.DiapersRepository
import dev.sobhy.babycareassistant.ui.composable.DiapersCardFromNotification
import dev.sobhy.babycareassistant.ui.composable.VaccinationCardFromNotification
import dev.sobhy.babycareassistant.ui.theme.BabyCareAssistantTheme
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import dev.sobhy.babycareassistant.vaccination.data.repository.VaccinationRepository
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActivity : ComponentActivity() {
    private var notificationData: Parcelable? = null
    private var timeIndex: Int? = null

    @Inject
    lateinit var vaccinationRepository: VaccinationRepository

    @Inject
    lateinit var diapersRepository: DiapersRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val data = intent?.getParcelableExtra<FeedingSchedule>("feedingSchedule")

        notificationData = intent?.getParcelableExtra("notificationData")
        timeIndex = intent?.getIntExtra("timeIndex", -1)?.takeIf { it >= 0 }
        setContent {
            BabyCareAssistantTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    data?.let {
                        Dialog(
                            onDismissRequest = {},
                            properties = DialogProperties(
                                usePlatformDefaultWidth = false,
                                dismissOnBackPress = false,
                                dismissOnClickOutside = false,
                            )
                        ) {
                            Card(
                                shape = MaterialTheme.shapes.small,
                                colors = CardDefaults.outlinedCardColors(
                                    containerColor = Color.White
                                ),
                                border = BorderStroke(1.dp, color = Color.Gray),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .padding(8.dp),
                            ) {
                                Text(text = "Breastfeeding Data", modifier = Modifier.padding(8.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(2.dp)
                                        .padding(horizontal = 8.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            shape = MaterialTheme.shapes.extraSmall
                                        )
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Date", color = Color.Gray)
                                    Text(text = LocalTime.now().toString())
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(2.dp)
                                        .padding(horizontal = 8.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            shape = MaterialTheme.shapes.extraSmall
                                        )
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Number of feedings/day", color = Color.Gray)
                                    Text(text = data.daily_feedings.toString())
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(2.dp)
                                        .padding(horizontal = 8.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            shape = MaterialTheme.shapes.extraSmall
                                        )
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "amount of milk", color = Color.Gray)
                                    Text(text = data.milk_quantity)
                                }
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ){
                                    Button(
                                        onClick = {
                                                  finish()
                                        },
                                        shape = MaterialTheme.shapes.small,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Gray
                                        )
                                    ) {
                                        Text(text = "Close")
                                    }
                                    Button(
                                        onClick = {
//                                            val userId = auth.currentUser?.uid
//                                            if (userId != null) {
//                                                firebaseService.fetchUserDateOfBirth(userId) { dateOfBirth ->
//                                                    val babyAgeInMonths = firebaseService.calculateBabyAge(dateOfBirth)
//                                                    firebaseService.fetchFeedingSchedule(babyAgeInMonths) { feedingSchedule ->
//                                                        Log.d("MainActivity", "Feeding Schedule: $feedingSchedule")
//                                                        AlarmManagerHelper.scheduleNextFeedingNotification(
//                                                            this@NotificationActivity,
//                                                            feedingSchedule,
//                                                            feedingSchedule.notes.split(" ")[1].toInt() // Interval in hours
//                                                        )
//                                                    }
//                                                }
//                                            }
                                            finish()
                                        },
                                        shape = MaterialTheme.shapes.small,
                                    ) {
                                        Text(text = "Done")
                                    }
                                }
                            }
                        }
                    }

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
                                        finish()
                                    },
                                    onError = { exception ->
                                        Log.e(
                                            "MainActivity",
                                            "Error updating vaccination status",
                                            exception
                                        )
                                    }
                                )
                                finish()
                            }
                            is Diapers -> DiapersCardFromNotification(
                                diapers = data,
                                indexOfColorChange = timeIndex!!,
                                modifier = Modifier.align(Alignment.Center),
                                later = { finish() }) {
                                diapersRepository.updateDiaperChangeTimeFromNotification(
                                    diapersId = data.id,
                                    index = timeIndex!!,
                                    newTime = LocalTime.now().toString()
                                )
                                finish()
                            }
                        }
                    }
                }

            }
        }
    }
}
