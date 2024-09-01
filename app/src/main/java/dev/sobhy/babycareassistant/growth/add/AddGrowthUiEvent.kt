package dev.sobhy.babycareassistant.growth.add

import java.time.LocalDate

sealed class AddGrowthUiEvent{
    data class BabyAgeChange(val age: Int): AddGrowthUiEvent()
    data class DataOfMeasurementChange(val date: LocalDate): AddGrowthUiEvent()
    data class BabyWeightChange(val weight: String): AddGrowthUiEvent()
    data class BabyHeightChange(val height: String): AddGrowthUiEvent()
    data class HeadCircumferenceChange(val headCircumference: String): AddGrowthUiEvent()
    data class AddGrowth(val growthId: String?): AddGrowthUiEvent()
}
