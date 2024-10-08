package dev.sobhy.babycareassistant.breastfeeding.data.repository

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.alarm.data.repository.AlarmManagerHelper
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FeedingRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val context: Context,
) {
    fun saveOrUpdateBreastFeed(breastFeed: BreastFeed): Task<Void> {
        val breastFeedCollection =
            firestore.collection("users")
                .document(firebaseAuth.currentUser!!.uid)
                .collection("breastfeeding")
        if (breastFeed.id.isNotEmpty()) {
            return breastFeedCollection.document(breastFeed.id).set(breastFeed)
                .addOnSuccessListener {
                    breastFeed.timeOfTimes.forEachIndexed { index, _ ->
                        AlarmManagerHelper.scheduleBreastFeedingAlarm(context, breastFeed, index)
                    }
                }
        } else {
            val documentReference = breastFeedCollection.document()
            val breastFeedWithId = breastFeed.copy(id = documentReference.id)
            return documentReference.set(breastFeedWithId).addOnSuccessListener {
                breastFeedWithId.timeOfTimes.forEachIndexed { index, _ ->
                    AlarmManagerHelper.scheduleBreastFeedingAlarm(context, breastFeedWithId, index)
                }
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
    }

    suspend fun getBreastFeedById(breastFeedId: String): BreastFeed? {
        val snapshot = firestore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("breastfeeding").document(breastFeedId).get().await()
        return snapshot.toObject(BreastFeed::class.java)
    }

    fun updateFeedingTimeFromNotification(
        docId: String,
        index: Int,
        newTime: String,
    ) {
        val docRef = firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("breastfeeding").document(docId)

        // Get the document containing the array
        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // Retrieve the 'times' array from Firestore
                    val timesArray = document.get("timeOfTimes") as? MutableList<Map<String, Any>>

                // Ensure the array and index are valid
                if (timesArray != null && timesArray.size > index) {
                    // Get the second element (index 1)
                    val elementShouldChange = timesArray[index].toMutableMap()

                    // Update the 'time' field of the second element
                    elementShouldChange["time"] = newTime

                    // Replace the second element in the array
                    timesArray[index] = elementShouldChange

                    // Update the 'times' array in Firestore
                    docRef.update("timeOfTimes", timesArray)
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