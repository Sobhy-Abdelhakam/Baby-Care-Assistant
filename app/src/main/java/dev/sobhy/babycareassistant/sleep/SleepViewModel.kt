package dev.sobhy.babycareassistant.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.growth.data.model.BabyGrowth
import dev.sobhy.babycareassistant.sleep.data.model.SleepData
import dev.sobhy.babycareassistant.sleep.domain.usecases.DeleteSleepUseCase
import dev.sobhy.babycareassistant.sleep.domain.usecases.GetSleepUseCase
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
class SleepViewModel @Inject constructor(
    private val getSleepDataUseCase: GetSleepUseCase,
    private val deleteSleepUseCase: DeleteSleepUseCase
): ViewModel() {
    private val _sleepState = MutableStateFlow(FeaturesScreenState<SleepData>())
    val growthState = _sleepState.asStateFlow()
    init {
        getSleeps()
    }

    private fun getSleeps(){
        viewModelScope.launch(Dispatchers.IO) {
            getSleepDataUseCase()
                .onStart {
                    _sleepState.value = FeaturesScreenState(loading = true)
                }
                .catch { e ->
                    _sleepState.update {
                        it.copy(
                            loading = false,
                            error = e.message
                        )
                    }
                }
                .collect{data ->
                    _sleepState.update {
                        it.copy(
                            loading = false,
                            data = data
                        )
                    }
                }
        }
    }
    fun deleteSleep(growthId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteSleepUseCase(growthId)
            } catch (e: Exception){
                _sleepState.update { it.copy(error = e.message) }
            }
        }
    }
}