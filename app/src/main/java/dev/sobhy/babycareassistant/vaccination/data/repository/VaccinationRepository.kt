package dev.sobhy.babycareassistant.vaccination.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.alarm.domain.model.AlarmData
import dev.sobhy.babycareassistant.alarm.domain.repository.AlarmManagerRepository
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.healthinfo.data.model.HealthInfo
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class VaccinationRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val alarmManagerRepository: AlarmManagerRepository
) {

    fun saveVaccination(vaccination: Vaccination): Task<Void> {
        val documentReference =
            firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                .collection("vaccinations").document()
        val vaccinationWithId = vaccination.copy(id = documentReference.id)
        return documentReference.set(vaccinationWithId).addOnSuccessListener {
//            scheduleAlarmsForVaccination(vaccinationWithId)
        }
    }

    suspend fun getVaccinations(): Flow<List<Vaccination>> = callbackFlow {
        val collectionRef = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("vaccinations")
        val subscription = collectionRef.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                close(e)
            } else {
                val vaccinations = snapshot.toObjects(Vaccination::class.java)
                trySend(vaccinations)
            }
        }
        awaitClose { subscription.remove() }
    }
//    private fun scheduleAlarmsForVaccination(vaccination: Vaccination) {
//            val dateTemp = LocalDate.parse(vaccination.date).atStartOfDay()
//            val alarmData = AlarmData(
//                id = vaccination.id,
//                title = "Vaccination Reminder",
//                message = "It's time for vaccination ${vaccination.name}, ${vaccination.code}.",
//                timeInMillis = dateTemp.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
//            )
//            alarmManagerRepository.scheduleAlarm(alarmData)
//    }
}