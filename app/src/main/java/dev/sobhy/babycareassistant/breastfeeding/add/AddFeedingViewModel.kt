package dev.sobhy.babycareassistant.breastfeeding.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.breastfeeding.data.model.FeedingTimes
import dev.sobhy.babycareassistant.breastfeeding.domain.usecases.GetFeedingByIdUseCase
import dev.sobhy.babycareassistant.breastfeeding.domain.usecases.SaveOrUpdateFeedingUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddFeedingViewModel @Inject constructor(
    private val saveFeedingUseCase: SaveOrUpdateFeedingUseCase,
    private val getFeedingByIdUseCase: GetFeedingByIdUseCase,
) : ViewModel() {
    private val _addFeedingState = MutableStateFlow(AddBreastFeedState())
    val addFeedingState = _addFeedingState.asStateFlow()

    fun onEvent(event: AddFeedingUiEvent) {
        when (event) {
            is AddFeedingUiEvent.FeedingDateChanged -> updateDate(event.date)
            is AddFeedingUiEvent.FeedingDayChanged -> updateDay(event.day)
            is AddFeedingUiEvent.NumberOfFeedingsChanged -> updateNumberOfFeedings(event.number)
            is AddFeedingUiEvent.AddFeedingClicked -> addOrUpdateFeeding(event.feedingId)
            is AddFeedingUiEvent.UpdateTimeFieldsValues -> {
                val newFieldValues = _addFeedingState.value.feedingTimes.toMutableList().apply {
                    this[event.index] = this[event.index].copy(feedingTime = event.value)
                }
                _addFeedingState.update {
                    it.copy(feedingTimes = newFieldValues)
                }
            }
            is AddFeedingUiEvent.UpdateAmountFieldsValues -> {
                val newFieldValues = _addFeedingState.value.feedingTimes.toMutableList().apply {
                    this[event.index] = this[event.index].copy(amountOfMilk = event.amount)
                }
                _addFeedingState.update {
                    it.copy(feedingTimes = newFieldValues)
                }
            }
        }
    }

    private fun updateDate(date: LocalDate) {
        _addFeedingState.update {
            it.copy(feedingDate = date)
        }
    }

    private fun updateDay(day: String) {
        _addFeedingState.update {
            it.copy(feedingDay = day)
        }
    }

    private fun updateNumberOfFeedings(number: String) {
        val newFieldsVal = if (number.isBlank()) {
            emptyList()
        } else {
            List(number.toInt()) { index ->
                _addFeedingState.value.feedingTimes.getOrNull(index) ?: FeedingTimeData()
            }
        }
        _addFeedingState.update {
            it.copy(
                numOfFeedingPerDay = number,
                feedingTimes = newFieldsVal
            )
        }
    }

    private fun addOrUpdateFeeding(id: String?) {
        val breastFeedingState = addFeedingState.value

        // Validate inputs
        if (!validateFeedingData(breastFeedingState)) return

        _addFeedingState.update { it.copy(loading = true) }

        val breastFeed = createBreastFeed(id, breastFeedingState)

        // Perform save operation
        viewModelScope.launch {
            saveFeedingUseCase(breastFeed)
                .addOnSuccessListener {
                    _addFeedingState.update { it.copy(loading = false, success = true) }
                }
                .addOnFailureListener { e ->
                    _addFeedingState.update { it.copy(loading = false, error = e.message) }
                }
        }
    }

    private fun validateFeedingData(state: AddBreastFeedState): Boolean {
        // Validate fields and return early if there are errors
        val errors = mapOf(
            "dateError" to state.feedingDate.isEqual(LocalDate.of(1000, 1, 1)),
            "numberOfFeedingPerDayError" to state.numOfFeedingPerDay.isBlank(),
        ).filterValues { it }

        if (errors.isNotEmpty()) {
            _addFeedingState.update {
                it.copy(
                    dateError = if (errors["dateError"] == true) "Date is required" else null,
                    numberOfFeedingPerDayError = if (errors["numberOfFeedingPerDayError"] == true) "Number of feeding per day is required" else null,
                )
            }
            return false
        }

        // Validate feeding times
        if (state.feedingTimes.any { it.feedingTime == LocalTime.MIDNIGHT || it.amountOfMilk.isBlank() }) {
            _addFeedingState.update { it.copy(error = "All time and amount fields are required") }
            return false
        }

        return true
    }

    private fun createBreastFeed(id: String?, state: AddBreastFeedState): BreastFeed {
        return BreastFeed(
            id = id ?: "",
            date = state.feedingDate.toString(),
            day = state.feedingDay,
            numberOfFeedingsPerDay = state.numOfFeedingPerDay.toInt(),
            timeOfTimes = state.feedingTimes.map { FeedingTimes(it.feedingTime.toString(), it.amountOfMilk.toInt()) }
        )
    }



    fun getFeedingById(id: String) {
        _addFeedingState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val breastFeed = getFeedingByIdUseCase(id)!!
                _addFeedingState.update {
                    it.copy(
                        feedingDate = LocalDate.parse(breastFeed.date),
                        feedingDay = breastFeed.day,
                        numOfFeedingPerDay = breastFeed.numberOfFeedingsPerDay.toString(),
                        feedingTimes = breastFeed.timeOfTimes.map { time ->
                            FeedingTimeData(LocalTime.parse(time.time), time.amountOfMilk.toString())
                        },
                        loading = false
                    )
                }
            } catch (e: Exception) {
                _addFeedingState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}