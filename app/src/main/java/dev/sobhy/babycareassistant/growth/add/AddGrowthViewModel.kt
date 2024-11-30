package dev.sobhy.babycareassistant.growth.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.growth.data.model.BabyGrowth
import dev.sobhy.babycareassistant.growth.domain.usecases.GetGrowthByIdUseCase
import dev.sobhy.babycareassistant.growth.domain.usecases.SaveOrUpdateGrowthUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddGrowthViewModel @Inject constructor(
    private val saveOrUpdateGrowthUseCase: SaveOrUpdateGrowthUseCase,
    private val getGrowthByIdUseCase: GetGrowthByIdUseCase
) : ViewModel() {
    private val _addGrowthState = MutableStateFlow(AddGrowthState())
    val addGrowthState = _addGrowthState.asStateFlow()
    fun onEvent(event: AddGrowthUiEvent){
        when(event){
            is AddGrowthUiEvent.BabyAgeChange -> {
                _addGrowthState.update {
                    it.copy(babyAge = event.age)
                }
            }
            is AddGrowthUiEvent.DataOfMeasurementChange -> {
                _addGrowthState.update {
                    it.copy(dateOfMeasurement = event.date)
                }
            }
            is AddGrowthUiEvent.BabyWeightChange -> {
                _addGrowthState.update {
                    it.copy(babyWeight = event.weight)
                }
            }
            is AddGrowthUiEvent.BabyHeightChange -> {
                _addGrowthState.update {
                    it.copy(babyHeight = event.height)
                }
            }
            is AddGrowthUiEvent.AddGrowth -> addOrUpdateFeeding(event.growthId)
            is AddGrowthUiEvent.HeadCircumferenceChange -> {
                _addGrowthState.update {
                    it.copy(headCircumference = event.headCircumference)
                }
            }
        }
    }
    private fun addOrUpdateFeeding(id: String?){
        val babyGrowthState = addGrowthState.value
        val errors = mapOf(
            "dateError" to babyGrowthState.dateOfMeasurement.isEqual(LocalDate.of(1000, 1, 1)),
            "weightError" to babyGrowthState.babyWeight.isBlank(),
            "heightError" to babyGrowthState.babyHeight.isBlank(),
            "headCircumferenceError" to babyGrowthState.headCircumference.isBlank()
        ).filterValues { it }
        if (errors.isNotEmpty()){
            _addGrowthState.update {
                it.copy(
                    dateError = if (errors["dateError"] == true) "Date is required" else null,
                    weightError = if (errors["weightError"] == true) "Weight is required" else null,
                    heightError = if (errors["heightError"] == true) "Height is required" else null,
                    headCircumferenceError = if (errors["headCircumferenceError"] == true) "Head circumference is required" else null
                )
            }
            return
        }
        _addGrowthState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val babyGrowth = BabyGrowth(
                id = id ?: "",
                dateOfMeasurement = babyGrowthState.dateOfMeasurement.toString(),
                age = babyGrowthState.babyAge,
                weight = babyGrowthState.babyWeight.toDouble(),
                height = babyGrowthState.babyHeight.toDouble(),
                headCircumference = babyGrowthState.headCircumference.toDouble()
            )
            saveOrUpdateGrowthUseCase(babyGrowth)
                .addOnSuccessListener {
                    _addGrowthState.update { it.copy(loading = false, success = true) }
                }
                .addOnFailureListener { e ->
                    _addGrowthState.update { it.copy(loading = false, error = e.message) }
                }
        }
    }
    fun getGrowthById(id: String){
        _addGrowthState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val babyGrowth = getGrowthByIdUseCase(id)!!
                _addGrowthState.update {
                    it.copy(
                        dateOfMeasurement = LocalDate.parse(babyGrowth.dateOfMeasurement),
                        babyAge = babyGrowth.age,
                        babyWeight = babyGrowth.weight.toString(),
                        babyHeight = babyGrowth.height.toString(),
                        headCircumference = babyGrowth.headCircumference.toString(),
                        loading = false
                    )
                }
            } catch (e: Exception){
                _addGrowthState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}