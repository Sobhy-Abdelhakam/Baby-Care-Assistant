package dev.sobhy.babycareassistant.healthinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.healthinfo.data.model.HealthInfo
import dev.sobhy.babycareassistant.healthinfo.usecases.DeleteHealthInfoUseCase
import dev.sobhy.babycareassistant.healthinfo.usecases.GetHealthInfoUseCase
import dev.sobhy.babycareassistant.ui.classes.FeaturesScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthInfoViewModel @Inject constructor(
    private val getHealthInfoUseCase: GetHealthInfoUseCase,
    private val deleteHealthInfoUseCase: DeleteHealthInfoUseCase,
) : ViewModel() {
    private val _healthInfoState = MutableStateFlow(FeaturesScreenState<HealthInfo>())
    val healthInfoState = _healthInfoState.asStateFlow()

    init {
        getHealthInfo()
    }

    private fun getHealthInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            getHealthInfoUseCase()
                .onStart {
                    _healthInfoState.update { it.copy(loading = true) }
                }
                .catch {e ->
                    _healthInfoState.update { it.copy(loading = false, error = e.message) }
                }
                .collectLatest { data ->
                    _healthInfoState.update { it.copy(loading = false, data = data) }
                }
        }
    }

    fun deleteHealthInfo(healthInfoId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteHealthInfoUseCase(healthInfoId)
            } catch (e: Exception) {
                _healthInfoState.update { it.copy(error = e.message) }
            }

        }
    }
}