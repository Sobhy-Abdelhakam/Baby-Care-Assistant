package dev.sobhy.babycareassistant.authentication.domain.usecase

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null,
)
