package dev.sobhy.babycareassistant.vaccination.addvaccination

import java.time.LocalDate

data class AddVaccinationState(
    val vaccinationName: String = "",
    val vaccinationDate: LocalDate = LocalDate.of(1000, 1, 1),
    val vaccinationDay: String = "",
    val vaccinationCode: String = "",
    val ageGroup: String = "Select",
    val vaccinationReason: String = "",
    val otherNotes: String = "",

    val nameError: String? = null,
    val dateError: String? = null,
    val codeError: String? = null,
    val ageGroupError: String? = null,
    val reasonError: String? = null,

    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
