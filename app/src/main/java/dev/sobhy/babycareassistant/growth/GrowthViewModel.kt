package dev.sobhy.babycareassistant.growth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.growth.data.model.BabyGrowth
import dev.sobhy.babycareassistant.growth.domain.usecases.CheckBabyGrowthUseCase
import dev.sobhy.babycareassistant.growth.domain.usecases.DeleteGrowthUseCase
import dev.sobhy.babycareassistant.growth.domain.usecases.GetGrowthUseCase
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
class GrowthViewModel @Inject constructor(
    private val getGrowthUseCase: GetGrowthUseCase,
    private val deleteGrowthUseCase: DeleteGrowthUseCase,
): ViewModel() {
    private val checkBabyGrowthUseCase: CheckBabyGrowthUseCase = CheckBabyGrowthUseCase()
    private val _growthState = MutableStateFlow(FeaturesScreenState<BabyGrowthWithStatus>())
    val growthState = _growthState.asStateFlow()
    init {
        getGrowth()
    }

    private fun getGrowth(){
        viewModelScope.launch(Dispatchers.IO) {
            getGrowthUseCase()
                .onStart {
                    _growthState.value = FeaturesScreenState(loading = true)
                }
                .catch { e ->
                    _growthState.update {
                        it.copy(
                            loading = false,
                            error = e.message
                        )
                    }
                }
                .collect{data ->
                    val growthWithStatusList = data.map { babyGrowth ->
                        val status = checkBabyGrowthUseCase.execute(babyGrowth)
                        BabyGrowthWithStatus(babyGrowth, status)
                    }
                    _growthState.update {
                        it.copy(
                            loading = false,
                            data = growthWithStatusList
                        )
                    }
                }
        }
    }
    fun deleteGrowth(growthId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteGrowthUseCase(growthId)
            } catch (e: Exception){
                _growthState.update { it.copy(error = e.message) }
            }
        }
    }
}