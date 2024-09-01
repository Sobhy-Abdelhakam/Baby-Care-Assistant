package dev.sobhy.babycareassistant.ui.classes

data class FeaturesScreenState<T>(
    val loading: Boolean = false,
    val data: List<T> = emptyList(),
    val error: String? = null,
)
