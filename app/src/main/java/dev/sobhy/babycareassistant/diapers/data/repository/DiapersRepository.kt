package dev.sobhy.babycareassistant.diapers.data.repository

import android.content.Context
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
    private val context: Context
) {
    fun saveOrUpdateDiapers(diapers: Diapers): Task<Void> {
        val diapersCollection =
            firestore.collection("users")
                .document(firebaseAuth.currentUser!!.uid)
                .collection("diapers")
        if (diapers.id.isNotEmpty()) {
            return diapersCollection.document(diapers.id).set(diapers).addOnSuccessListener {
                diapers.timesOfDiapersChange.forEachIndexed { index, _ ->
                    AlarmManagerHelper.scheduleDiaperChangeAlarm(context, diapers, index)
                }
            }
        } else {
            val documentReference = diapersCollection.document()
            val diapersWithId = diapers.copy(id = documentReference.id)
            return documentReference.set(diapersWithId).addOnSuccessListener {
                diapersWithId.timesOfDiapersChange.forEachIndexed { index, _ ->
                    AlarmManagerHelper.scheduleDiaperChangeAlarm(context, diapersWithId, index)
                }
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
    }

    suspend fun getDiapersById(diapersId: String): Diapers? {
        val snapshot = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("diapers").document(diapersId).get().await()
        return snapshot.toObject(Diapers::class.java)
    }
}