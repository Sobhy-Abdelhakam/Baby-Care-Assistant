package dev.sobhy.babycareassistant.growth.domain.usecases

import dev.sobhy.babycareassistant.growth.data.model.BabyGrowth
import dev.sobhy.babycareassistant.growth.data.model.GrowthStandard

class CheckBabyGrowthUseCase {
    private val growthStandards = mapOf(
        1 to GrowthStandard(2.5f, 4.5f, 45.0f, 55.0f, 32.0f, 37.0f),
        2 to GrowthStandard(3.5f, 6.0f, 50.0f, 60.0f, 34.0f, 39.0f),
        3 to GrowthStandard(4.0f, 7.0f, 53.0f, 63.0f, 36.0f, 41.0f),
        4 to GrowthStandard(4.5f, 8.0f, 55.0f, 65.0f, 37.0f, 42.0f),
        5 to GrowthStandard(5.0f, 8.5f, 57.0f, 67.0f, 38.0f, 43.0f),
        6 to GrowthStandard(5.5f, 9.5f, 59.0f, 69.0f, 39.0f, 44.0f),
        7 to GrowthStandard(6.0f, 10.0f, 61.0f, 71.0f, 40.0f, 45.0f),
        8 to GrowthStandard(6.5f, 10.5f, 62.0f, 72.0f, 41.0f, 46.0f),
        9 to GrowthStandard(7.0f, 11.0f, 64.0f, 74.0f, 42.0f, 47.0f),
        10 to GrowthStandard(7.5f, 11.5f, 65.0f, 76.0f, 43.0f, 48.0f),
        11 to GrowthStandard(8.0f, 12.0f, 67.0f, 78.0f, 44.0f, 49.0f),
        12 to GrowthStandard(8.5f, 12.5f, 68.0f, 80.0f, 45.0f, 50.0f)
    )
    fun execute(babyGrowth: BabyGrowth): String{
        val standard = growthStandards[babyGrowth.age]
        return if (standard != null
            && babyGrowth.weight in standard.minWeight..standard.maxWeight
            && babyGrowth.height in standard.minHeight..standard.maxHeight
            && babyGrowth.headCircumference in standard.minHeadCircumference..standard.maxHeadCircumference
            ){
            "This growth is normal"
        } else {
            "Needs to see a doctor"
        }
    }
}