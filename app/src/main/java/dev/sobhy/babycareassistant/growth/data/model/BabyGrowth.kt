package dev.sobhy.babycareassistant.growth.data.model

data class BabyGrowth(
    val id: String = "",
    val age: Int = 0,
    val dateOfMeasurement: String = "",
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val headCircumference: Double = 0.0,
)
