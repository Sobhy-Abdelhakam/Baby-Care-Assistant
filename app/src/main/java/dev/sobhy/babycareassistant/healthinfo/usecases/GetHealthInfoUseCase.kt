package dev.sobhy.babycareassistant.healthinfo.usecases

import dev.sobhy.babycareassistant.healthinfo.data.repository.HealthInfoRepository

class GetHealthInfoUseCase(
    private val healthInfoRepository: HealthInfoRepository,
) {
    suspend operator fun invoke() = healthInfoRepository.getHealthInfo()
}