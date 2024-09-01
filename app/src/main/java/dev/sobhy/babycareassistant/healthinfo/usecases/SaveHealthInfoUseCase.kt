package dev.sobhy.babycareassistant.healthinfo.usecases

import com.google.android.gms.tasks.Task
import dev.sobhy.babycareassistant.healthinfo.data.model.HealthInfo
import dev.sobhy.babycareassistant.healthinfo.data.repository.HealthInfoRepository

class SaveHealthInfoUseCase(
    private val healthInfoRepository: HealthInfoRepository
) {
    operator fun invoke(healthInfo: HealthInfo): Task<Void> {
        return healthInfoRepository.saveOrUpdateHealthInfo(healthInfo)
    }
}