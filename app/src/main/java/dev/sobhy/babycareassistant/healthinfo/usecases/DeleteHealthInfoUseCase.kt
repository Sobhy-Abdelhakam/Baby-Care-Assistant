package dev.sobhy.babycareassistant.healthinfo.usecases

import dev.sobhy.babycareassistant.healthinfo.data.repository.HealthInfoRepository

class DeleteHealthInfoUseCase(
    private val healthInfoRepository: HealthInfoRepository
) {
    suspend operator fun invoke(id: String) {
        healthInfoRepository.deleteHealthInfo(id)
    }
}