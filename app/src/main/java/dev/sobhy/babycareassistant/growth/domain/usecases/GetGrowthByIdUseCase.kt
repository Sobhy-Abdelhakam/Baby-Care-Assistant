package dev.sobhy.babycareassistant.growth.domain.usecases

import dev.sobhy.babycareassistant.growth.data.repository.GrowthRepository

class GetGrowthByIdUseCase(
    private val growthRepository: GrowthRepository
) {
    suspend operator fun invoke(id: String) = growthRepository.getBabyGrowthById(id)
}