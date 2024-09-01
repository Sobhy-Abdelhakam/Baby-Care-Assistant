package dev.sobhy.babycareassistant.vaccination.addvaccination

import java.time.LocalDate

sealed class AddVaccinationEvent{
    data class VaccinationNameChange(val name: String): AddVaccinationEvent()
    data class VaccinationDateChange(val date: LocalDate): AddVaccinationEvent()
    data class VaccinationDayChange(val day: String): AddVaccinationEvent()
    data class VaccinationCodeChange(val code: String): AddVaccinationEvent()
    data class AgeGroupChange(val ageGroup: String): AddVaccinationEvent()
    data class VaccinationReasonChange(val reason: String): AddVaccinationEvent()
    data class OtherNotesChange(val notes: String): AddVaccinationEvent()
    data object Submit: AddVaccinationEvent()
}
