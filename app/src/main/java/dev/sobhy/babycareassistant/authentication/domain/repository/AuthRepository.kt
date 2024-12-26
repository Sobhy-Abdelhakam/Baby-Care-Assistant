package dev.sobhy.babycareassistant.authentication.domain.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dev.sobhy.babycareassistant.alarm.data.repository.AlarmManagerHelper
import dev.sobhy.babycareassistant.authentication.domain.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val alarmManagerHelper: AlarmManagerHelper,
) {

    suspend fun register(
        fullName: String,
        email: String,
        gender: String,
        dateOfBirth: String,
        age: Int,
        password: String,
        imageUri: Uri,
    ): Flow<Result<String>> = flow {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val userId = authResult.user?.uid ?: throw Exception("User ID is null")
        // Upload user data
        val profileImageUrl = uploadImage(userId, imageUri)
        saveUserData(userId, fullName, email, gender, dateOfBirth, age, profileImageUrl)
        alarmManagerHelper.initialFeedingNotification()

        emit(Result.success("Registration successful"))
    }.catch { e ->
        emit(Result.failure(e))
    }
//    private fun scheduleInitialNotification(age: Int) {
//        fetchFeedingSchedule(age){feedingSchedule ->
//            alarmManagerHelper.scheduleFeedingNotification(
//                feedingSchedule,
//                0 // Interval in hours
//            )
//        }
//    }

    private suspend fun uploadImage(userId: String, imageUri: Uri): String {
        val imageRef = storage.reference.child("profile_images/$userId.jpg")
        imageRef.putFile(imageUri).await()
        return imageRef.downloadUrl.await().toString()
    }

    private suspend fun saveUserData(
        userId: String,
        fullName: String,
        email: String,
        gender: String,
        dateOfBirth: String,
        age: Int,
        imageUri: String,
    ) {
        val userData = mapOf(
            "fullName" to fullName,
            "email" to email,
            "gender" to gender,
            "dateOfBirth" to dateOfBirth,
            "age" to age,
            "profileImage" to imageUri
        )
        db.collection("users").document(userId).set(userData).await()
    }

    fun login(
        email: String,
        password: String
    ): Flow<Result<String>> = flow {
        auth.signInWithEmailAndPassword(email, password).await()

        // Emit success result
        emit(Result.success("Login successful"))
    }.catch { e ->
        // Emit error result
        emit(Result.failure(e))
    }
    fun logout() {
        auth.signOut()
    }
    fun getUserProfile(): Flow<Result<UserProfile>> = flow {
        val uid = auth.currentUser?.uid ?: throw Exception("User ID is null")
        val userProfile = db.collection("users").document(uid).get().await().toObject(UserProfile::class.java)
        emit(Result.success(userProfile!!))
    }.catch { e ->
        emit(Result.failure(e))
    }

//    fun fetchFeedingSchedule(babyAgeInMonths: Int, onComplete: (FeedingSchedule) -> Unit) {
//        val documentId = when (babyAgeInMonths) {
//            1 -> "month_1"
//            2 -> "month_2"
//            in 3..4 -> "month_3_4"
//            in 5..6 -> "month_5_6"
//            in 7..9 -> "month_7_9"
//            in 10..12 -> "month_10_12"
//            else -> null
//        }
//
//        documentId?.let {docId ->
//            db.collection("feeding_schedule").document(docId).get()
//                .addOnSuccessListener { document ->
//                    document.toObject(FeedingSchedule::class.java)?.let { onComplete(it) }
//                }
//        }
//    }

}