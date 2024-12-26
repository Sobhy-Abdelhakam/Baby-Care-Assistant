package dev.sobhy.babycareassistant.notification.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sobhy.babycareassistant.notification.domain.Notification

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NotificationRepository(
    firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore,

    ) {
    private val userDocRef = firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
    private val notificationCollectionRef = userDocRef.collection("notification")
    suspend fun saveNotification(notification: Notification){
        notificationCollectionRef.document().set(notification).await()
    }
    suspend fun getNotifications(): Flow<List<Notification>> = callbackFlow {
        val subscription = notificationCollectionRef.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                close(e)
            } else {
                val notification = snapshot.toObjects(Notification::class.java)
                trySend(notification)
            }
        }
        awaitClose { subscription.remove() }
    }
}