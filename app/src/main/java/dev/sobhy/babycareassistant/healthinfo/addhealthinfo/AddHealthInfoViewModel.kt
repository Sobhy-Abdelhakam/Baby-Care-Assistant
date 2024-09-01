package dev.sobhy.babycareassistant.healthinfo.addhealthinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.healthinfo.data.model.HealthInfo
import dev.sobhy.babycareassistant.healthinfo.usecases.GetHealthInfoById
import dev.sobhy.babycareassistant.healthinfo.usecases.SaveHealthInfoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddHealthInfoViewModel @Inject constructor(
    private val saveHealthInfoUseCase: SaveHealthInfoUseCase,
    private val getHealthInfoUseCase: GetHealthInfoById,
): ViewModel() {
    private val _addHealthInfoState = MutableStateFlow(AddHealthInfoState())
    val addHealthInfoState = _addHealthInfoState.asStateFlow()
    fun onEvent(event: AddHealthInfoEvent){
        when(event){
            is AddHealthInfoEvent.DateChange -> {
                _addHealthInfoState.update {
                    it.copy(date = event.date)
                }
            }
            is AddHealthInfoEvent.ExplanationChange -> {
                _addHealthInfoState.update {
                    it.copy(explanation = event.explanation)
                }
            }
            is AddHealthInfoEvent.NoteChange -> {
                _addHealthInfoState.update {
                    it.copy(note = event.note)
                }
            }
            is AddHealthInfoEvent.Add -> addOrUpdateHealthInfo(event.healthInfoId)
        }
    }
    private fun addOrUpdateHealthInfo(id: String?){
        val healthInfoState = addHealthInfoState.value
        val errors = mapOf(
            "dateError" to healthInfoState.date.isEqual(LocalDate.of(1000, 1, 1)),
            "explanationError" to healthInfoState.explanation.isBlank(),
            "noteError" to healthInfoState.note.isBlank()
        ).filterValues { it }
        if (errors.isNotEmpty()){
            _addHealthInfoState.update {
                it.copy(
                    dateError = if (errors["dateError"] == true) "Date is required" else null,
                    explanationError = if (errors["explanationError"] == true) "Explanation is required" else null,
                    noteError = if (errors["noteError"] == true) "Note is required" else null
                )
            }
            return
        }
        _addHealthInfoState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val healthInfo = HealthInfo(
                healthInfoId = id ?: "",
                healthInfoDate = healthInfoState.date.toString(),
                healthInfoExplanation = healthInfoState.explanation,
                healthInfoNote = healthInfoState.note
            )
            saveHealthInfoUseCase(healthInfo)
                .addOnSuccessListener {
                    _addHealthInfoState.update { it.copy(loading = false, success = true) }
                }
                .addOnFailureListener { e ->
                    _addHealthInfoState.update { it.copy(loading = false, error = e.message) }
                }
        }
    }

    fun getHealthInfoById(id: String){
        _addHealthInfoState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val healthInfo = getHealthInfoUseCase(id)!!
                _addHealthInfoState.update {
                    it.copy(
                        date = LocalDate.parse(healthInfo.healthInfoDate),
                        explanation = healthInfo.healthInfoExplanation,
                        note = healthInfo.healthInfoNote,
                        loading = false
                    )
                }
            } catch (e: Exception){
                _addHealthInfoState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}