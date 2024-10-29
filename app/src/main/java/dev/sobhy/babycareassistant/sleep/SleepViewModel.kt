package dev.sobhy.babycareassistant.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val deleteSleepUseCase: DeleteSleepUseCase,
) : ViewModel() {
    private val _sleepState = MutableStateFlow(FeaturesScreenState<SleepData>())
    val sleepState = _sleepState.asStateFlow()

    private val _selectedSleepResult = MutableStateFlow(SleepResult())
    val selectedSleepResult = _selectedSleepResult.asStateFlow()

    init {
        getSleeps()
    }

    private fun getSleeps() {
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
                .collect { data ->
                    _sleepState.update {
                        it.copy(
                            loading = false,
                            data = data
                        )
                    }
                }
        }
    }

    // Calculate the result based on total sleep duration and baby age group
    fun calculateSleepResult(sleepData: SleepData) {
        val totalSleepTime = sleepData.totalSleepTime // Total sleep time in hours
        val resultMessage = when (sleepData.babyAge) {
            "0 - 3 months" -> {
                when {
                    totalSleepTime < 14 -> SleepResult(
                        result = "Less than normal",
                        advice = listOf(
                            "May affect growth and development.",
                            "Ensure a quiet and comfortable environment for sleep and increase feeding sessions."
                        )
                    )

                    totalSleepTime > 17 -> SleepResult(
                        result = "More than normal",
                        advice = listOf(
                            "May indicate health problems.",
                            "Consult a doctor if excessive sleep is observed."
                        )
                    )

                    else -> SleepResult(
                        result = "Normal sleep duration for this age group."
                    )
                }
            }

            "4 - 11 months" -> {
                when {
                    totalSleepTime < 12 -> SleepResult(
                        result = "Less than normal",
                        advice = listOf(
                            "Mood disorders and growth delays may occur.",
                            "Establish a consistent sleep schedule and ensure proper nutrition."
                        )
                    )

                    totalSleepTime > 15 -> SleepResult(
                        result = "More than normal",
                        advice = listOf(
                            "Excessive sleep may affect feeding.",
                            "Ensure the baby is getting adequate nutrition, consult a doctor if needed."
                        )
                    )

                    else -> SleepResult(
                        result = "Normal sleep duration for this age group."
                    )
                }
            }

            "1 - 2 years" -> {
                when {
                    totalSleepTime < 11 -> SleepResult(
                        result = "Less than normal",
                        advice = listOf(
                            "May lead to behavioral disorders and decreased attention.",
                            "Ensure daily sleep routine and increase physical activities during the day."
                        )
                    )

                    totalSleepTime > 14 -> SleepResult(
                        result = "More than normal",
                        advice = listOf(
                            "May be a sign of health problems such as anemia.",
                            "Ensure the baby gets balanced nutrition, consult a doctor if necessary."
                        )
                    )

                    else -> SleepResult(
                        result = "Normal sleep duration for this age group."
                    )
                }
            }

            "3 - 5 years" -> {
                when {
                    totalSleepTime < 10 -> SleepResult(
                        result = "Less than normal",
                        advice = listOf(
                            "May cause behavioral disorders and difficulty learning.",
                            "Encourage physical activities and reduce long naps."
                        )
                    )

                    totalSleepTime > 13 -> SleepResult(
                        result = "More than normal",
                        advice = listOf(
                            "May indicate insufficient daily activity.",
                            "Ensure the child is active during the day and monitor their diet."
                        )
                    )

                    else -> SleepResult(
                        result = "Normal sleep duration for this age group."
                    )
                }
            }

            else -> SleepResult(
                result = "No data available for this age group."
            )

        }
        _selectedSleepResult.value = resultMessage
    }

    fun deleteSleep(growthId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteSleepUseCase(growthId)
            } catch (e: Exception) {
                _sleepState.update { it.copy(error = e.message) }
            }
        }
    }
}

data class SleepResult(
    val result: String = "",
    val advice: List<String> = emptyList(),
)