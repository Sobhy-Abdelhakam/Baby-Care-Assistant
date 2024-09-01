package dev.sobhy.babycareassistant.breastfeeding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.breastfeeding.domain.usecases.DeleteFeedingUseCase
import dev.sobhy.babycareassistant.breastfeeding.domain.usecases.GetFeedingUseCase
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
class FeedingViewModel @Inject constructor(
    private val getFeedingUseCase: GetFeedingUseCase,
    private val deleteFeedingUseCase: DeleteFeedingUseCase,
) : ViewModel(){
    private val _feedingState = MutableStateFlow(FeaturesScreenState<BreastFeed>())
    val feedingState = _feedingState.asStateFlow()

    init {
        getFeeding()
    }

    private fun getFeeding(){
        viewModelScope.launch(Dispatchers.IO) {
            getFeedingUseCase()
                .onStart {
                    _feedingState.value = FeaturesScreenState(loading = true)
                }
                .catch { e ->
                    _feedingState.update {
                        it.copy(
                            loading = false,
                            error = e.message
                        )
                    }
                }
                .collect{data ->
                    _feedingState.update {
                        it.copy(
                            loading = false,
                            data = data
                        )
                    }
                }
        }
    }

    fun deleteFeeding(feedingId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteFeedingUseCase(feedingId)
            } catch (e: Exception){
                _feedingState.update { it.copy(error = e.message) }
            }
        }
    }
}