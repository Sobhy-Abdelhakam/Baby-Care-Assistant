package dev.sobhy.babycareassistant.diapers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.diapers.domain.usecase.DeleteDiaperUseCase
import dev.sobhy.babycareassistant.diapers.domain.usecase.GetDiapersUseCase
import dev.sobhy.babycareassistant.ui.classes.FeaturesScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiapersViewModel @Inject constructor(
    private val getDiapersUseCase: GetDiapersUseCase,
    private val deleteDiaperUseCase: DeleteDiaperUseCase
): ViewModel() {
    private val _diapersState = MutableStateFlow(FeaturesScreenState<Diapers>())
    val diapersState = _diapersState.asStateFlow()

    init {
        getDiapers()
    }

    private fun getDiapers(){
        viewModelScope.launch(Dispatchers.IO) {
            getDiapersUseCase()
                .onStart {
                    _diapersState.value = FeaturesScreenState(loading = true)
                }
                .catch { e ->
                    _diapersState.update {
                        it.copy(
                            loading = false,
                            error = e.message
                        )
                    }
                }
                .collect{data ->
                    _diapersState.update {
                        it.copy(
                            loading = false,
                            data = data
                        )
                    }
                }
        }
    }

    fun deleteDiaper(diaperId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteDiaperUseCase(diaperId)
            } catch (e: Exception){
                _diapersState.update { it.copy(error = e.message) }
            }
        }
    }
}