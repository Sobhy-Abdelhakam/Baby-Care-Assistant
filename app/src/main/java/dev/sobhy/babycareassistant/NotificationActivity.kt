package dev.sobhy.babycareassistant

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.diapers.data.repository.DiapersRepository
import dev.sobhy.babycareassistant.ui.composable.DiapersCardFromNotification
import dev.sobhy.babycareassistant.ui.composable.FeedingCardFromNotification
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
    lateinit var breastFeedRepository: FeedingRepository

    @Inject
    lateinit var diapersRepository: DiapersRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        notificationData = intent?.getParcelableExtra("notificationData")
        timeIndex = intent?.getIntExtra("timeIndex", -1)?.takeIf { it >= 0 }
        setContent {
            BabyCareAssistantTheme {
                Box(modifier = Modifier.fillMaxSize()) {
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
                            }

                            is BreastFeed -> FeedingCardFromNotification(
                                breastFeed = data,
                                indexOfColorChange = timeIndex!!,
                                modifier = Modifier.align(Alignment.Center),
                                later = { finish() }) {
                                breastFeedRepository.updateFeedingTimeFromNotification(
                                    docId = data.id,
                                    index = timeIndex!!,
                                    newTime = LocalTime.now().toString()
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
