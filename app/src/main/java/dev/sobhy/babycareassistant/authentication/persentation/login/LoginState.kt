package dev.sobhy.babycareassistant.authentication.persentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)
