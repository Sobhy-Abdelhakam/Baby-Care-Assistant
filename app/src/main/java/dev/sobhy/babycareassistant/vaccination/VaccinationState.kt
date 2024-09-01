package dev.sobhy.babycareassistant.vaccination

import dev.sobhy.babycareassistant.vaccination.data.Vaccination

data class VaccinationState(
    val isLoading: Boolean = false,
    val vaccinationList: List<Vaccination> = emptyList(),
    val error: String? = null,
)
