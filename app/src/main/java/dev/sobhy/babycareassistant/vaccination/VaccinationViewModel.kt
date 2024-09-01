package dev.sobhy.babycareassistant.vaccination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.ui.classes.FeaturesScreenState
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import dev.sobhy.babycareassistant.vaccination.domain.usecases.GetVaccinationsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaccinationViewModel @Inject constructor(
    private val getVaccinationsUseCase: GetVaccinationsUseCase
): ViewModel() {
    private val _vaccinations = MutableStateFlow(FeaturesScreenState<Vaccination>())
    val vaccinations = _vaccinations.asStateFlow()

    init {
        getVaccinations()
    }

    private fun getVaccinations() {
        _vaccinations.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            getVaccinationsUseCase().onStart {
                _vaccinations.update { it.copy(loading = true) }
            }.catch { e ->
                _vaccinations.update { it.copy(loading = false, error = e.message) }
            }.collect {data ->
                _vaccinations.update { it.copy(loading = false, data = data) }
            }
        }
    }
}