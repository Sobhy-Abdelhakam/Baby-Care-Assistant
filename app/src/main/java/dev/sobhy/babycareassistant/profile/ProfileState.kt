package dev.sobhy.babycareassistant.profile

import dev.sobhy.babycareassistant.authentication.domain.UserProfile

data class ProfileState(
    val isLoading: Boolean = false,
    val user: UserProfile? = null,
    val error: String? = null
)
