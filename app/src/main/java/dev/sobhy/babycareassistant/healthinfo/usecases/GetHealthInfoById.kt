package dev.sobhy.babycareassistant.healthinfo.usecases

import dev.sobhy.babycareassistant.healthinfo.data.repository.HealthInfoRepository

class GetHealthInfoById(
    private val healthInfoRepository: HealthInfoRepository
) {
    suspend operator fun invoke(id: String) = healthInfoRepository.getHealthInfoById(id)
}