package dev.sobhy.babycareassistant.sleep.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.growth.data.model.BabyGrowth
import dev.sobhy.babycareassistant.sleep.data.model.SleepData
import dev.sobhy.babycareassistant.sleep.data.model.SleepTime
import dev.sobhy.babycareassistant.sleep.domain.usecases.GetSleepByIdUseCase
import dev.sobhy.babycareassistant.sleep.domain.usecases.SaveOrUpdateSleepUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddSleepViewModel @Inject constructor(
    private val saveOrUpdateSleepUseCase: SaveOrUpdateSleepUseCase,
    private val getSleepByIdUseCase: GetSleepByIdUseCase,
) : ViewModel() {
    private val _addSleepState = MutableStateFlow(AddSleepState())
    val addSleepState = _addSleepState.asStateFlow()
    fun onEvent(event: AddSleepUiEvent) {
        when (event) {
            is AddSleepUiEvent.DateOfSleepChanged -> {
                _addSleepState.update { it.copy(date = event.date, dateError = null) }
            }

            is AddSleepUiEvent.SleepTimeChange -> {
                _addSleepState.update { it.copy(sleepTime = event.time) }
            }

            is AddSleepUiEvent.WakeUpTimeChange -> {
                _addSleepState.update { it.copy(wakeUpTime = event.time) }
            }

            is AddSleepUiEvent.DurationChange -> {
                _addSleepState.update { it.copy(duration = event.duration) }
            }

            AddSleepUiEvent.AddSleepTime -> addSleepTimeItem()
            is AddSleepUiEvent.SaveSleepData -> saveSleepData(event.sleepId)
            is AddSleepUiEvent.OnDeleteSleepTimeClicked -> {
                val updatedSleepTimes = _addSleepState.value.sleepTimesList.toMutableList().apply {
                    removeAt(event.index)
                }
                _addSleepState.value = _addSleepState.value.copy(
                    sleepTimesList = updatedSleepTimes,
                )
            }
        }
    }

    private fun addSleepTimeItem() {
        _addSleepState.update {
            it.copy(
                sleepTimesList = it.sleepTimesList.plus(
                    SleepTime(
                        addSleepState.value.sleepTime.toString(),
                        addSleepState.value.wakeUpTime.toString(),
                        addSleepState.value.duration
                    )
                ),
                sleepTime = LocalTime.of(10, 0),
                wakeUpTime = LocalTime.of(10, 0),
                duration = "",
                sleepTimesError = null
            )
        }
    }
    private fun saveSleepData(id: String?){
        val sleepState = addSleepState.value
        val errors = mapOf(
            "dateError" to sleepState.date.isEqual(LocalDate.of(1000, 1, 1)),
            "sleepTimesError" to sleepState.sleepTimesList.isEmpty(),
        ).filterValues { it }
        if (errors.isNotEmpty()){
            _addSleepState.update {
                it.copy(
                    dateError = if (errors["dateError"] == true) "Date is required" else null,
                    sleepTimesError = if (errors["sleepTimesError"] == true) "Sleep times are required" else null,
                )
            }
            return
        }
        _addSleepState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val babySleep = SleepData(
                id = id ?: "",
                date = sleepState.date.toString(),
                sleepTimes = sleepState.sleepTimesList
            )
            saveOrUpdateSleepUseCase(babySleep)
                .addOnSuccessListener {
                    _addSleepState.update { it.copy(loading = false, success = true) }
                }
                .addOnFailureListener { e ->
                    _addSleepState.update { it.copy(loading = false, error = e.message) }
                }
        }
    }

    fun getSleepById(id: String){
        _addSleepState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val sleepData = getSleepByIdUseCase(id)!!
                _addSleepState.update {
                    it.copy(
                        date = LocalDate.parse(sleepData.date),
                        sleepTimesList = sleepData.sleepTimes,
                        loading = false
                    )
                }
            } catch (e: Exception){
                _addSleepState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}