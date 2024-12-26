package dev.sobhy.babycareassistant.authentication.persentation.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.authentication.domain.repository.AuthRepository
import dev.sobhy.babycareassistant.authentication.domain.usecase.ValidateEmail
import dev.sobhy.babycareassistant.authentication.domain.usecase.ValidatePassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val validateEmail: ValidateEmail = ValidateEmail()
    private val validatePassword: ValidatePassword = ValidatePassword()
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.ImageChange -> _registerState.update { it.copy(image = event.image) }
            is RegisterUiEvent.DOBChange -> {
                _registerState.update {
                    it.copy(
                        dateOfBirth = event.dateOfBirth,
                        age = calculateBabyAge(event.dateOfBirth.toString())
                    )
                }
            }
            is RegisterUiEvent.AgeChange -> _registerState.update { it.copy(age = event.age) }
            is RegisterUiEvent.EmailChange -> _registerState.update { it.copy(email = event.email) }
            is RegisterUiEvent.GenderChange -> _registerState.update { it.copy(gender = event.gender) }
            is RegisterUiEvent.FullNameChange -> _registerState.update { it.copy(fullName = event.name) }
            is RegisterUiEvent.PasswordChange -> _registerState.update { it.copy(password = event.password) }
            is RegisterUiEvent.ConfirmPasswordChange -> _registerState.update {
                it.copy(
                    confirmPassword = event.confirmPassword
                )
            }

            RegisterUiEvent.Register -> register()
        }
    }

    fun register() {
        val validationErrors = validateInputs()

        if (validationErrors.hasErrors) {
            showValidationErrors(validationErrors)
            return
        }
        _registerState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.register(
                fullName = registerState.value.fullName,
                email = registerState.value.email,
                password = registerState.value.password,
                dateOfBirth = registerState.value.dateOfBirth.toString(),
                age = registerState.value.age,
                gender = registerState.value.gender,
                imageUri = registerState.value.image!!,
            ).collectLatest { result ->
                handleRegistrationResult(result)
            }
        }
    }

    private fun validateInputs(): ValidationErrors {
        val imageError = registerState.value.image == null
        val fullNameError = registerState.value.fullName.isBlank()
        val emailResult = validateEmail.execute(registerState.value.email)
        val birthdayError = registerState.value.dateOfBirth.isEqual(LocalDate.of(1000, 1, 1))
        val passwordResult = validatePassword.execute(registerState.value.password)
        val confirmPasswordError = registerState.value.password != registerState.value.confirmPassword

        return ValidationErrors(
            imageError = imageError,
            fullNameError = fullNameError,
            emailError = emailResult.errorMessage,
            passwordError = passwordResult.errorMessage,
            confirmPasswordError = if (confirmPasswordError) "Passwords don't match" else null,
            dateOfBirthError = if (birthdayError) "Please select your date of birth" else null,
            hasErrors = imageError || fullNameError || !emailResult.successful || !passwordResult.successful || confirmPasswordError || birthdayError
        )
    }
    private fun showValidationErrors(errors: ValidationErrors) {
        _registerState.update {
            it.copy(
                imageError = if (errors.imageError) "Please select an image" else null,
                fullNameError = if (errors.fullNameError) "The name can't be empty" else null,
                emailError = errors.emailError,
                passwordError = errors.passwordError,
                confirmPasswordError = errors.confirmPasswordError,
                dateOfBirthError = errors.dateOfBirthError
            )
        }
    }
    private fun handleRegistrationResult(result: Result<String>) {
        if (result.isSuccess) {
            val message = result.getOrNull()
            _registerState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    success = true
                )
            }
            Log.d("RegisterViewModel", "register: $message")
        } else {
            val errorMessage = result.exceptionOrNull()?.message
            _registerState.update { it.copy(isLoading = false, error = errorMessage) }
            Log.d("RegisterViewModel", "register: $errorMessage")
        }
    }
    private fun calculateBabyAge(dateOfBirth: String): Int{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dob = LocalDate.parse(dateOfBirth, formatter)
        val now = LocalDate.now()
        return Period.between(dob, now).months +1
    }
}
