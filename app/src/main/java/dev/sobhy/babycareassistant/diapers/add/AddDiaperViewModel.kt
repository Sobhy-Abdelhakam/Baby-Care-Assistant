package dev.sobhy.babycareassistant.diapers.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.diapers.domain.usecase.GetDiaperByIdUseCase
import dev.sobhy.babycareassistant.diapers.domain.usecase.SaveOrUpdateDiaperUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddDiaperViewModel @Inject constructor(
    private val saveOrUpdateDiaperUseCase: SaveOrUpdateDiaperUseCase,
    private val getDiaperByIdUseCase: GetDiaperByIdUseCase,
): ViewModel() {
    private val _addDiaperState = MutableStateFlow(AddDiaperState())
    val addDiaperState = _addDiaperState.asStateFlow()
    fun onEvent(event: AddDiaperUiEvent){
        when(event){
            is AddDiaperUiEvent.DiaperDateChanged -> {
                _addDiaperState.update {
                    it.copy(diaperDate = event.date)
                }
            }
            is AddDiaperUiEvent.DiaperDayChanged -> _addDiaperState.update {
                it.copy(diaperDay = event.day)
            }
            is AddDiaperUiEvent.NumberOfDiaperChanged -> {
                val newFieldsVal = if (event.number.isBlank()) emptyList() else List(event.number.toInt()){index ->
                    _addDiaperState.value.timesOfDiaperChange.getOrNull(index) ?: LocalTime.of(0, 0,30)
                }
                _addDiaperState.update {
                    it.copy(
                        numOfDiaperChange = event.number,
                        timesOfDiaperChange = newFieldsVal
                    )
                }
            }
            is AddDiaperUiEvent.UpdateTimeFieldsValues -> {
                val newFieldValues = _addDiaperState.value.timesOfDiaperChange.toMutableList().apply {
                    this[event.index] = event.value
                }
                _addDiaperState.update {
                    it.copy(
                        timesOfDiaperChange = newFieldValues
                    )
                }
            }
            is AddDiaperUiEvent.AddDiaperClicked -> addOrUpdateDiaper(event.diaperId)
        }
    }
    private fun addOrUpdateDiaper(id: String?){
        val diapersState = addDiaperState.value
        val errors = mapOf(
            "dateError" to diapersState.diaperDate.isEqual(LocalDate.of(1000, 1, 1)),
            "numberOfDiaperChangeError" to diapersState.numOfDiaperChange.isBlank(),
        ).filterValues { it }
        if (errors.isNotEmpty()){
            _addDiaperState.update {
                it.copy(
                    dateError = if (errors["dateError"] == true) "Date is required" else null,
                    numOfDiaperChangeError = if (errors["numberOfDiaperChangeError"] == true) "number of diaper change is required" else null,
                )
            }
            return
        }
        if (diapersState.timesOfDiaperChange.any { it == LocalTime.of(0,0,30) }){
            _addDiaperState.update {
                it.copy(
                    error = "All time fields are required"
                )
            }
            return
        }
        _addDiaperState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val diaper = Diapers(
                id = id ?: "",
                date = diapersState.diaperDate.toString(),
                day = diapersState.diaperDay,
                numberOfDiapersChange = diapersState.numOfDiaperChange.toInt(),
                timesOfDiapersChange = diapersState.timesOfDiaperChange.map { it.toString() }
            )
            saveOrUpdateDiaperUseCase(diaper)
                .addOnSuccessListener {
                    _addDiaperState.update { it.copy(loading = false, success = true) }
                }
                .addOnFailureListener { e ->
                    _addDiaperState.update { it.copy(loading = false, error = e.message) }
                }
        }
    }
    fun getDiaperById(id: String){
        _addDiaperState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val breastFeed = getDiaperByIdUseCase(id)!!
                _addDiaperState.update {
                    it.copy(
                        diaperDate = LocalDate.parse(breastFeed.date),
                        diaperDay = breastFeed.day,
                        numOfDiaperChange = breastFeed.numberOfDiapersChange.toString(),
                        timesOfDiaperChange = breastFeed.timesOfDiapersChange.map {time ->LocalTime.parse(time) },
                        loading = false
                    )
                }
            } catch (e: Exception){
                _addDiaperState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

}