package dev.sobhy.babycareassistant.growth.add

import java.time.LocalDate

data class AddGrowthState(
    val babyAge: Int = 1,
    val dateOfMeasurement: LocalDate = LocalDate.of(1000, 1, 1),
    val babyWeight: String = "",
    val babyHeight: String = "",
    val headCircumference: String = "",

    val dateError: String? = null,
    val weightError: String? = null,
    val heightError: String? = null,
    val headCircumferenceError: String? = null,

    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
