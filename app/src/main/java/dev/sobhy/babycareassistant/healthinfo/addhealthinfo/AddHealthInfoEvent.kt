package dev.sobhy.babycareassistant.healthinfo.addhealthinfo

import java.time.LocalDate

sealed class AddHealthInfoEvent{
    data class DateChange(val date: LocalDate) : AddHealthInfoEvent()
    data class ExplanationChange(val explanation: String) : AddHealthInfoEvent()
    data class NoteChange(val note: String) : AddHealthInfoEvent()
    class Add(val healthInfoId: String?) : AddHealthInfoEvent()
}
