package dev.sobhy.babycareassistant.growth.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.growth.data.model.BabyGrowth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class GrowthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
){
    fun saveOrUpdateBabyGrowth(babyGrowth: BabyGrowth): Task<Void> {
        val babyGrowthCollection =
            firestore.collection("users")
                .document(firebaseAuth.currentUser!!.uid)
                .collection("growth")
        if (babyGrowth.id.isNotEmpty()) {
            return babyGrowthCollection.document(babyGrowth.id).set(babyGrowth)
        } else {
            val documentReference = babyGrowthCollection.document()
            val babyGrowthWithId = babyGrowth.copy(id = documentReference.id)
            return documentReference.set(babyGrowthWithId)
        }
    }
    suspend fun getBabyGrowth(): Flow<List<BabyGrowth>> = callbackFlow {
        val collectionRef = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("growth")
        val subscription = collectionRef.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                close(e)
            } else {
                val babyGrowth = snapshot.toObjects(BabyGrowth::class.java)
                trySend(babyGrowth)
            }
        }
        awaitClose { subscription.remove() }
    }
    suspend fun deleteBabyGrowth(babyGrowthId: String) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("growth").document(babyGrowthId).delete().await()
    }
    suspend fun getBabyGrowthById(babyGrowthId: String): BabyGrowth? {
        val snapshot = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("growth").document(babyGrowthId).get().await()
        return snapshot.toObject(BabyGrowth::class.java)
    }
}