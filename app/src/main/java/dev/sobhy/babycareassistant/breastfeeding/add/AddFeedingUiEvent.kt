package dev.sobhy.babycareassistant.breastfeeding.add

import java.time.LocalDate
import java.time.LocalTime

sealed class AddFeedingUiEvent{
    data class FeedingDateChanged(val date: LocalDate) : AddFeedingUiEvent()
    data class FeedingDayChanged(val day: String) : AddFeedingUiEvent()
    data class NumberOfFeedingsChanged(val number: String) : AddFeedingUiEvent()
    data class UpdateTimeFieldsValues(val index: Int, val value: LocalTime): AddFeedingUiEvent()
    data class UpdateAmountFieldsValues(val index: Int, val amount: String): AddFeedingUiEvent()

    data class AddFeedingClicked(val feedingId: String?) : AddFeedingUiEvent()
}
