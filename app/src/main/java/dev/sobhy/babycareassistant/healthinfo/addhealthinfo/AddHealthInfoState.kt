package dev.sobhy.babycareassistant.healthinfo.addhealthinfo

import java.time.LocalDate

data class AddHealthInfoState(
    val date: LocalDate = LocalDate.of(1000, 1, 1),
    val explanation: String = "",
    val note: String = "",

    val dateError: String? = null,
    val explanationError: String? = null,
    val noteError: String? = null,

    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
