package dev.sobhy.babycareassistant.diapers.data.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.alarm.data.repository.AlarmManagerHelper
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DiapersRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val alarmManagerHelper: AlarmManagerHelper,
) {
    private val diapersCollectionRef = firestore.collection("users")
        .document(firebaseAuth.currentUser!!.uid)
        .collection("diapers")

    fun saveOrUpdateDiapers(diapers: Diapers): Task<Void> {
        if (diapers.id.isNotEmpty()) {
            return diapersCollectionRef.document(diapers.id).set(diapers).addOnSuccessListener {
                diapers.timesOfDiapersChange.forEachIndexed { index, _ ->
                    alarmManagerHelper.scheduleDiaperChangeAlarm(diapers, index)
                }
            }
        } else {
            val documentReference = diapersCollectionRef.document()
            val diapersWithId = diapers.copy(id = documentReference.id)
            return documentReference.set(diapersWithId).addOnSuccessListener {
                diapersWithId.timesOfDiapersChange.forEachIndexed { index, _ ->
                    alarmManagerHelper.scheduleDiaperChangeAlarm(diapersWithId, index)
                }
            }
        }
    }

    suspend fun getDiapers(): Flow<List<Diapers>> = callbackFlow {
        val subscription = diapersCollectionRef.addSnapshotListener { snapshot, e ->
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
    }

    suspend fun getDiapersById(diapersId: String): Diapers? {
        val snapshot = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("diapers").document(diapersId).get().await()
        return snapshot.toObject(Diapers::class.java)
    }

    fun updateDiaperChangeTimeFromNotification(
        diapersId: String,
        index: Int,
        newTime: String,
    ) {
        val documentReference = diapersCollectionRef.document(diapersId)
        documentReference.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val timesArray = document.get("timesOfDiapersChange") as? MutableList<String>
                // ensure the array and index are valid
                if (timesArray != null && timesArray.size > index) {
                    timesArray[index] = newTime
                    documentReference.update("timesOfDiapersChange", timesArray)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Document successfully updated!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error updating document", e)
                        }
                } else {
                    Log.w("Firestore", "Array or index is invalid")
                }
            } else {
                Log.w("Firestore", "Document does not exist")
            }
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error getting document", e)
        }
    }
}