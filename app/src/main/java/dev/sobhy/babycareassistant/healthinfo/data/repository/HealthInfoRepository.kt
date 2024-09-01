package dev.sobhy.babycareassistant.healthinfo.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.healthinfo.data.model.HealthInfo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HealthInfoRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) {

    fun saveOrUpdateHealthInfo(healthInfo: HealthInfo): Task<Void> {
        val healthInfoCollection =
            firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                .collection("healthInfo")
        if (healthInfo.healthInfoId.isNotEmpty()) {
            return healthInfoCollection.document(healthInfo.healthInfoId).set(healthInfo)
        } else {
            val documentReference = healthInfoCollection.document()
            val healthInfoWithId = healthInfo.copy(healthInfoId = documentReference.id)
            return documentReference.set(healthInfoWithId)
        }
    }

    suspend fun getHealthInfo(): Flow<List<HealthInfo>> = callbackFlow {
        val collectionRef = firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("healthInfo")
        val subscription = collectionRef.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                close(e)
            } else {
                val healthInfo = snapshot.toObjects(HealthInfo::class.java)
                trySend(healthInfo)
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun deleteHealthInfo(healthInfoId: String) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("healthInfo").document(healthInfoId).delete().await()
    }

    suspend fun getHealthInfoById(healthInfoId: String): HealthInfo? {
        val snapshot = firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("healthInfo").document(healthInfoId).get().await()
        return snapshot.toObject(HealthInfo::class.java)
    }


}