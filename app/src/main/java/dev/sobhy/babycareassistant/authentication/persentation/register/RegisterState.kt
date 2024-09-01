package dev.sobhy.babycareassistant.authentication.persentation.register

import android.net.Uri
import java.time.LocalDate

data class RegisterState(
    val image: Uri? = null,
    val fullName: String = "",
    val email: String = "",
    val gender: String = "Select",
    val dateOfBirth: LocalDate = LocalDate.of(1000, 1, 1),
    val age: Int = 0,
    val password: String = "",
    val confirmPassword: String = "",
    val imageError: String? = null,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val genderError: String? = null,
    val dateOfBirthError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)
