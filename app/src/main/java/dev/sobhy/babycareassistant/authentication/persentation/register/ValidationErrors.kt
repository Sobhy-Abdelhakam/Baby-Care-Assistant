package dev.sobhy.babycareassistant.authentication.persentation.register

data class ValidationErrors(
    val imageError: Boolean,
    val fullNameError: Boolean,
    val emailError: String?,
    val passwordError: String?,
    val confirmPasswordError: String?,
    val dateOfBirthError: String?,
    val hasErrors: Boolean
)