package dev.sobhy.babycareassistant.sleep.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.sleep.data.model.SleepData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SleepRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    fun saveOrUpdateSleep(sleepData: SleepData): Task<Void> {
        val sleepCollection =
            firestore.collection("users")
                .document(firebaseAuth.currentUser!!.uid)
                .collection("sleep")
        if (sleepData.id.isNotEmpty()) {
            return sleepCollection.document(sleepData.id).set(sleepData)
        } else {
            val documentReference = sleepCollection.document()
            val sleepWithId = sleepData.copy(id = documentReference.id)
            return documentReference.set(sleepWithId)
        }
    }
    suspend fun getSleeps(): Flow<List<SleepData>> = callbackFlow {
        val collectionRef = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("sleep")
        val subscription = collectionRef.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                close(e)
            } else {
                val sleep = snapshot.toObjects(SleepData::class.java)
                trySend(sleep)
            }
        }
        awaitClose { subscription.remove() }
    }
    suspend fun deleteSleep(sleepId: String) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("sleep").document(sleepId).delete().await()
    }
    suspend fun getSleepById(sleepId: String): SleepData? {
        val snapshot = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("sleep").document(sleepId).get().await()
        return snapshot.toObject(SleepData::class.java)
    }
}