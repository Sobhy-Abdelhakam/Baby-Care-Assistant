package dev.sobhy.babycareassistant.growth

import dev.sobhy.babycareassistant.growth.data.model.BabyGrowth

data class BabyGrowthWithStatus(
    val babyGrowth: BabyGrowth,
    val status: String
)
