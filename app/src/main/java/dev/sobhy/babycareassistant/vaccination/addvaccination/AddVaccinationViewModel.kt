package dev.sobhy.babycareassistant.vaccination.addvaccination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import dev.sobhy.babycareassistant.vaccination.domain.usecases.SaveVaccineUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddVaccinationViewModel @Inject constructor(
    private val saveVaccineUseCase: SaveVaccineUseCase
): ViewModel() {
    private val _addVaccinationState = MutableStateFlow(AddVaccinationState())
    val addVaccinationState = _addVaccinationState.asStateFlow()
    fun onEvent(event: AddVaccinationEvent) {
        when (event) {
            is AddVaccinationEvent.VaccinationNameChange -> updateName(event.name)
            is AddVaccinationEvent.VaccinationDateChange -> updateDate(event.date)
            is AddVaccinationEvent.VaccinationDayChange -> updateDay(event.day)
            is AddVaccinationEvent.VaccinationCodeChange -> updateCode(event.code)
            is AddVaccinationEvent.AgeGroupChange -> updateAgeGroup(event.ageGroup)
            is AddVaccinationEvent.VaccinationReasonChange -> updateReason(event.reason)
            is AddVaccinationEvent.OtherNotesChange -> updateNotes(event.notes)
            AddVaccinationEvent.Submit -> addVaccination()
        }
    }

    private fun addVaccination() {
        val vaccinationState = addVaccinationState.value
        val errors = mapOf(
            "nameError" to vaccinationState.vaccinationName.isBlank(),
            "dateError" to vaccinationState.vaccinationDate.isEqual(LocalDate.of(1000, 1, 1)),
            "codeError" to vaccinationState.vaccinationCode.isBlank(),
            "ageGroupError" to (vaccinationState.ageGroup == "Select"),
            "reasonError" to vaccinationState.vaccinationReason.isBlank()
        ).filterValues { it }

        if (errors.isNotEmpty()) {
            _addVaccinationState.update {
                it.copy(
                    nameError = if (errors["nameError"] == true) "Name is required" else null,
                    dateError = if (errors["dateError"] == true) "Date is required" else null,
                    codeError = if (errors["codeError"] == true) "Code is required" else null,
                    ageGroupError = if (errors["ageGroupError"] == true) "Age group is required" else null,
                    reasonError = if (errors["reasonError"] == true) "Reason is required" else null
                )
            }
            return
        }

        _addVaccinationState.update { it.copy(loading = true) }
        val vaccination = vaccinationState.toVaccination()
        viewModelScope.launch(Dispatchers.IO) {
            saveVaccineUseCase(vaccination)
                .addOnSuccessListener {
                    _addVaccinationState.update { it.copy(loading = false, success = true) }
                }
                .addOnFailureListener { e ->
                    _addVaccinationState.update { it.copy(loading = false, error = e.message) }
                }
        }
    }
    private fun AddVaccinationState.toVaccination(): Vaccination {
        return Vaccination(
            name = vaccinationName,
            code = vaccinationCode,
            date = vaccinationDate.toString(),
            day = vaccinationDay,
            ageGroup = ageGroup,
            reason = vaccinationReason,
            notes = otherNotes,
            done = false
        )
    }

    private fun updateName(name: String) {
        _addVaccinationState.update {
            it.copy(vaccinationName = name)
        }
    }

    private fun updateCode(code: String) {
        _addVaccinationState.update {
            it.copy(vaccinationCode = code)
        }
    }
    private fun updateAgeGroup(ageGroup: String) {
        _addVaccinationState.update {
            it.copy(ageGroup = ageGroup)
        }
    }

    private fun updateDate(date: LocalDate) {
        _addVaccinationState.update {
            it.copy(vaccinationDate = date)
        }
    }

    private fun updateDay(day: String) {
        _addVaccinationState.update {
            it.copy(vaccinationDay = day)
        }
    }

    private fun updateReason(reason: String) {
        _addVaccinationState.update {
            it.copy(vaccinationReason = reason)
        }
    }

    private fun updateNotes(notes: String) {
        _addVaccinationState.update {
            it.copy(otherNotes = notes)
        }
    }
}