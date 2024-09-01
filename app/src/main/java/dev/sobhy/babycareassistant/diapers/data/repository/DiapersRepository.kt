package dev.sobhy.babycareassistant.diapers.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.alarm.domain.model.AlarmData
import dev.sobhy.babycareassistant.alarm.domain.repository.AlarmManagerRepository
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class DiapersRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val alarmManagerRepository: AlarmManagerRepository
) {
    fun saveOrUpdateDiapers(diapers: Diapers): Task<Void> {
        val diapersCollection =
            firestore.collection("users")
                .document(firebaseAuth.currentUser!!.uid)
                .collection("diapers")
        if (diapers.id.isNotEmpty()) {
            return diapersCollection.document(diapers.id).set(diapers).addOnSuccessListener {
                scheduleAlarmsForDiapers(diapers)
            }
        } else {
            val documentReference = diapersCollection.document()
            val diapersWithId = diapers.copy(id = documentReference.id)
            return documentReference.set(diapersWithId).addOnSuccessListener {
                scheduleAlarmsForDiapers(diapersWithId)
            }
        }
    }
    suspend fun getDiapers(): Flow<List<Diapers>> = callbackFlow {
        val collectionRef = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("diapers")
        val subscription = collectionRef.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                close(e)
            } else {
                val diapers = snapshot.toObjects(Diapers::class.java)
                trySend(diapers)
            }
        }
        awaitClose { subscription.remove() }
    }
    suspend fun deleteDiapers(diapersId: String) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("diapers").document(diapersId).delete().await()
        alarmManagerRepository.cancelAlarm(diapersId)
    }

    suspend fun getDiapersById(diapersId: String): Diapers? {
        val snapshot = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("diapers").document(diapersId).get().await()
        return snapshot.toObject(Diapers::class.java)
    }

    private fun scheduleAlarmsForDiapers(diapers: Diapers) {
        diapers.timesOfDiapersChange.forEachIndexed { index, diaperTime ->
            val dateTemp = LocalDate.parse(diapers.date)
            val timeTemp = LocalTime.parse(diaperTime)
            val dateTimeTemp = LocalDateTime.of(dateTemp, timeTemp)
            val alarmData = AlarmData(
                id = diapers.id,
                title = "Diaper Change Reminder",
                message = "It's time for your diaper change num${index + 1}.",
                data = diapers,
                timeInMillis = dateTimeTemp.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
            )
            alarmManagerRepository.scheduleAlarm(alarmData)
        }
    }
}