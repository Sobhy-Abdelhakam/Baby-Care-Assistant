package dev.sobhy.babycareassistant.growth.domain.usecases

import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository
import dev.sobhy.babycareassistant.growth.data.repository.GrowthRepository

class DeleteGrowthUseCase(
    private val growthRepository: GrowthRepository
) {
    suspend operator fun invoke(growthId: String) = growthRepository.deleteBabyGrowth(growthId)
}