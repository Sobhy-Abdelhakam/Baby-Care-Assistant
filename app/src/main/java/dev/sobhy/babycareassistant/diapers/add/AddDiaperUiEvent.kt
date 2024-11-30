package dev.sobhy.babycareassistant.diapers.add

import java.time.LocalDate
import java.time.LocalTime

sealed class AddDiaperUiEvent {
    data class DiaperDateChanged(val date: LocalDate) : AddDiaperUiEvent()
    data class DiaperDayChanged(val day: String) : AddDiaperUiEvent()
    data class NumberOfDiaperChanged(val number: String) : AddDiaperUiEvent()

    data class UpdateTimeFieldsValues(val index: Int, val value: LocalTime): AddDiaperUiEvent()

    data class AddDiaperClicked(val diaperId: String?) : AddDiaperUiEvent()
}
