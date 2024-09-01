package dev.sobhy.babycareassistant

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainViewModel(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
): ViewModel() {
    val currentUser = firebaseAuth.currentUser
}