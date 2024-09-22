package dev.sobhy.babycareassistant.vaccination.data.repository

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.alarm.data.repository.AlarmManagerHelper
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class VaccinationRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    val context: Context
) {

    fun saveVaccination(vaccination: Vaccination): Task<Void> {
        val documentReference =
            firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                .collection("vaccinations").document()
        val vaccinationWithId = vaccination.copy(id = documentReference.id)
        return documentReference.set(vaccinationWithId).addOnSuccessListener {
            AlarmManagerHelper.scheduleVaccinationAlarm(context, vaccinationWithId)
        }
    }
    fun updateVaccinationStatus(vaccinationId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("vaccinations")
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
}