package dev.sobhy.babycareassistant.sleep.domain.usecases

import dev.sobhy.babycareassistant.sleep.data.model.SleepData
import dev.sobhy.babycareassistant.sleep.data.repository.SleepRepository

class SaveOrUpdateSleepUseCase(
    private val sleepRepository: SleepRepository
) {
    operator fun invoke(sleepData: SleepData) = sleepRepository.saveOrUpdateSleep(sleepData)
}