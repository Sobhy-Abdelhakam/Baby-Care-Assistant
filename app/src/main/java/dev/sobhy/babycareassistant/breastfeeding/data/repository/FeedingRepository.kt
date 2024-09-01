package dev.sobhy.babycareassistant.breastfeeding.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.alarm.domain.model.AlarmData
import dev.sobhy.babycareassistant.alarm.domain.repository.AlarmManagerRepository
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class FeedingRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val alarmManagerRepository: AlarmManagerRepository
) {
    fun saveOrUpdateBreastFeed(breastFeed: BreastFeed): Task<Void> {
        val breastFeedCollection =
            firestore.collection("users")
                .document(firebaseAuth.currentUser!!.uid)
                .collection("breastfeeding")
        if (breastFeed.id.isNotEmpty()) {
            return breastFeedCollection.document(breastFeed.id).set(breastFeed).addOnSuccessListener {
                scheduleAlarmsForBreastFeed(breastFeed)
            }
        } else {
            val documentReference = breastFeedCollection.document()
            val breastFeedWithId = breastFeed.copy(id = documentReference.id)
            return documentReference.set(breastFeedWithId).addOnSuccessListener {
                scheduleAlarmsForBreastFeed(breastFeedWithId)
            }
        }
    }
    suspend fun getBreastFeed(): Flow<List<BreastFeed>> = callbackFlow {
        val collectionRef = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("breastfeeding")
        val subscription = collectionRef.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                close(e)
            } else {
                val breastFeed = snapshot.toObjects(BreastFeed::class.java)
                trySend(breastFeed)
            }
        }
        awaitClose { subscription.remove() }
    }
    suspend fun deleteBreastFeed(breastFeedId: String) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("breastfeeding").document(breastFeedId).delete().await()
        alarmManagerRepository.cancelAlarm(breastFeedId)
    }

    suspend fun getBreastFeedById(breastFeedId: String): BreastFeed? {
        val snapshot = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("breastfeeding").document(breastFeedId).get().await()
        return snapshot.toObject(BreastFeed::class.java)
    }

    private fun scheduleAlarmsForBreastFeed(breastFeed: BreastFeed) {
        breastFeed.timeOfTimes.forEachIndexed { index, feedingTime ->
            val dateTemp = LocalDate.parse(breastFeed.date)
            val timeTemp = LocalTime.parse(feedingTime)
            val dateTimeTemp = LocalDateTime.of(dateTemp, timeTemp)
            val alarmData = AlarmData(
                id = breastFeed.id,
                title = "Breastfeeding Reminder",
                message = "It's time for your child's feeding num${index + 1}.",
                data = breastFeed,
                timeInMillis = dateTimeTemp.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
            )
            alarmManagerRepository.scheduleAlarm(alarmData)
        }
    }
}