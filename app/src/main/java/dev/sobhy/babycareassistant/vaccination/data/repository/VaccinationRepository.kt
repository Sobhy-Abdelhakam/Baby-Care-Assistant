package dev.sobhy.babycareassistant.vaccination.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.alarm.data.repository.AlarmManagerHelper
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class VaccinationRepository(
    firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore,
    private val alarmManagerHelper: AlarmManagerHelper,
) {

    private val userDocRef = firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
    private val vaccinationCollectionRef = userDocRef.collection("vaccinations")

    fun saveVaccination(vaccination: Vaccination): Task<Void> {
        val vaccinationDocRef = vaccinationCollectionRef.document()
        val vaccinationWithId = vaccination.copy(id = vaccinationDocRef.id)
        return vaccinationDocRef.set(vaccinationWithId).addOnSuccessListener {
            alarmManagerHelper.scheduleVaccinationAlarm(vaccinationWithId)
        }
    }
    fun updateVaccinationStatus(vaccinationId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        vaccinationCollectionRef
            .document(vaccinationId) // Update the specific vaccination document
            .update("done", true) // Update the "status" field
            .addOnSuccessListener {
                onSuccess() // Call onSuccess if the update is successful
            }
            .addOnFailureListener { exception ->
                onError(exception) // Handle errors
            }
    }

    suspend fun getVaccinations(): Flow<List<Vaccination>> = callbackFlow {
        val subscription = vaccinationCollectionRef.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                close(e)
            } else {
                val vaccinations = snapshot.toObjects(Vaccination::class.java)
                trySend(vaccinations)
            }
        }
        awaitClose { subscription.remove() }
    }
}