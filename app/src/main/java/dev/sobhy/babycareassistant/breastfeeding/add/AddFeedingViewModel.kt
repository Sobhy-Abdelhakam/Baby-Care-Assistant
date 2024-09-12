package dev.sobhy.babycareassistant.breastfeeding.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
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
): ViewModel() {
    private val _addFeedingState = MutableStateFlow(AddBreastFeedState())
    val addFeedingState = _addFeedingState.asStateFlow()

    fun onEvent(event: AddFeedingUiEvent) {
        when(event){
            is AddFeedingUiEvent.FeedingDateChanged -> updateDate(event.date)
            is AddFeedingUiEvent.FeedingDayChanged -> updateDay(event.day)
            is AddFeedingUiEvent.NumberOfFeedingsChanged -> {
                val newFieldsVal = if (event.number.isBlank()) emptyList() else List(event.number.toInt()){index ->
                    _addFeedingState.value.timesValues.getOrNull(index) ?: LocalTime.of(0, 0, 30)
                }
                _addFeedingState.update {
                    it.copy(
                        numOfFeedingPerDay = event.number,
                        timesValues = newFieldsVal
                    )
                }
            }
            is AddFeedingUiEvent.AmountOfMilkChanged -> updateAmountOfMilk(event.amount)
            is AddFeedingUiEvent.AddFeedingClicked -> addOrUpdateFeeding(event.feedingId)
            is AddFeedingUiEvent.UpdateTimeFieldsValues -> {
                val newFieldValues = _addFeedingState.value.timesValues.toMutableList().apply {
                    this[event.index] = event.value
                }
                _addFeedingState.update {
                    it.copy(
                        timesValues = newFieldValues
                    )
                }
                Log.d("TAG", "onEvent: ${_addFeedingState.value.timesValues}")
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
    private fun updateNumberOfFeedings(numberOfFeedings: String) {
        _addFeedingState.update {
            it.copy(
                numOfFeedingPerDay = numberOfFeedings,
            )
        }
    }
    private fun updateAmountOfMilk(amountOfMilk: String) {
        _addFeedingState.update {
            it.copy(amountOfFeedingPerTime = amountOfMilk)
        }
    }

    private fun addOrUpdateFeeding(id: String?){
        val breastFeedingState = addFeedingState.value
        val errors = mapOf(
            "dateError" to breastFeedingState.feedingDate.isEqual(LocalDate.of(1000, 1, 1)),
            "numberOfFeedingPerDayError" to breastFeedingState.numOfFeedingPerDay.isBlank(),
            "amountOfFeedingPerTimeError" to breastFeedingState.amountOfFeedingPerTime.isBlank()
        ).filterValues { it }
        if (errors.isNotEmpty()){
            _addFeedingState.update {
                it.copy(
                    dateError = if (errors["dateError"] == true) "Date is required" else null,
                    numberOfFeedingPerDayError = if (errors["numberOfFeedingPerDayError"] == true) "number of feeding per day is required" else null,
                    amountPerTimeError = if (errors["amountOfFeedingPerTimeError"] == true) "amount of feeding per time is required" else null
                )
            }
            return
        }
        if (breastFeedingState.timesValues.any { it == LocalTime.of(0,0,30) }){
            _addFeedingState.update {
                it.copy(
                    error = "All time fields are required"
                )
            }
            return
        }
        _addFeedingState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val breastFeed = BreastFeed(
                id = id ?: "",
                date = breastFeedingState.feedingDate.toString(),
                day = breastFeedingState.feedingDay,
                numberOfFeedingsPerDay = breastFeedingState.numOfFeedingPerDay.toInt(),
                amountOfMilkPerTime = breastFeedingState.amountOfFeedingPerTime.toInt(),
                timeOfTimes = breastFeedingState.timesValues.map { it.toString() }
            )
            saveFeedingUseCase(breastFeed)
                .addOnSuccessListener {
                    _addFeedingState.update { it.copy(loading = false, success = true) }
                }
                .addOnFailureListener { e ->
                    _addFeedingState.update { it.copy(loading = false, error = e.message) }
                }
        }
    }

    fun getFeedingById(id: String){
        _addFeedingState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val breastFeed = getFeedingByIdUseCase(id)!!
                _addFeedingState.update {
                    it.copy(
                        feedingDate = LocalDate.parse(breastFeed.date),
                        feedingDay = breastFeed.day,
                        numOfFeedingPerDay = breastFeed.numberOfFeedingsPerDay.toString(),
                        timesValues = breastFeed.timeOfTimes.map {time ->LocalTime.parse(time) },
                        amountOfFeedingPerTime = breastFeed.amountOfMilkPerTime.toString(),
                        loading = false
                    )
                }
            } catch (e: Exception){
                _addFeedingState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}