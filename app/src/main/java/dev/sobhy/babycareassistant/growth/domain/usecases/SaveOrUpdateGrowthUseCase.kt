package dev.sobhy.babycareassistant.growth.domain.usecases

import dev.sobhy.babycareassistant.growth.data.model.BabyGrowth
import dev.sobhy.babycareassistant.growth.data.repository.GrowthRepository

class SaveOrUpdateGrowthUseCase(
    private val growthRepository: GrowthRepository
) {
    operator fun invoke(babyGrowth: BabyGrowth) = growthRepository.saveOrUpdateBabyGrowth(babyGrowth)
}