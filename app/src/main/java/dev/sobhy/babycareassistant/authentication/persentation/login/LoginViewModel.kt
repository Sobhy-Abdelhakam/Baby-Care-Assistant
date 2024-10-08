package dev.sobhy.babycareassistant.authentication.persentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
        private val validateEmail: ValidateEmail = ValidateEmail()
        private val validatePassword: ValidatePassword = ValidatePassword()
        var email by mutableStateOf("")
            private set
        var emailError: String? by mutableStateOf(null)
            private set
        var password by mutableStateOf("")
            private set
        var passwordError: String? by mutableStateOf(null)
            private set

        private val _loginState = MutableStateFlow(LoginState())
        val loginState = _loginState.asStateFlow()

        fun onEvent(event: LoginUiEvent) {
            when (event) {
                is LoginUiEvent.UpdateEmail -> updateEmail(event.email)
                is LoginUiEvent.UpdatePassword -> updatePass(event.password)
                LoginUiEvent.Login -> login()
            }
        }

        private fun login() {
            val emailResult = validateEmail.execute(email)
            val passwordResult = validatePassword.execute(password)
            val hasError = listOf(emailResult, passwordResult).any { !it.successful }

            if (hasError) {
                emailError = emailResult.errorMessage
                passwordError = passwordResult.errorMessage
                return
            }
            _loginState.update { it.copy(isLoading = true) }
            viewModelScope.launch(Dispatchers.IO) {
                authRepository.login(email = email, password = password)
                    .collectLatest { result ->
                        if (result.isSuccess){
                            _loginState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = true,
                                    error = null
                                )
                            }
                        } else {
                            _loginState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = false,
                                    error = result.exceptionOrNull()?.message
                                    )
                            }
                        }
                    }
            }
        }

        private fun updateEmail(input: String) {
            emailError = null
            email = input
        }

        private fun updatePass(input: String) {
            passwordError = null
            password = input
        }
    }
