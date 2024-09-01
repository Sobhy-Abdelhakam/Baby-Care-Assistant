package dev.sobhy.babycareassistant.authentication.persentation.register

import android.net.Uri
import java.time.LocalDate

sealed class RegisterUiEvent {
    data class ImageChange(val image: Uri): RegisterUiEvent()
    data class FullNameChange(val name: String) : RegisterUiEvent()

    data class EmailChange(val email: String) : RegisterUiEvent()

    data class GenderChange(val gender: String) : RegisterUiEvent()

    data class DOBChange(val dateOfBirth: LocalDate) : RegisterUiEvent()

    data class AgeChange(val age: Int): RegisterUiEvent()

    data class PasswordChange(val password: String) : RegisterUiEvent()

    data class ConfirmPasswordChange(val confirmPassword: String) : RegisterUiEvent()

    data object Register : RegisterUiEvent()
}
