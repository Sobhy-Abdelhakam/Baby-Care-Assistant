package dev.sobhy.babycareassistant.authentication.domain

data class UserProfile(
    val fullName: String = "",
    val email: String = "",
    val gender: String = "",
    val dateOfBirth: String = "",
    val age: Int = 0,
    val profileImage: String = ""
)