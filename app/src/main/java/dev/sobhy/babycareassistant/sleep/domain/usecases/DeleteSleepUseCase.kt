package dev.sobhy.babycareassistant.sleep.domain.usecases

import dev.sobhy.babycareassistant.sleep.data.repository.SleepRepository

class DeleteSleepUseCase(
    private val sleepRepository: SleepRepository
) {
    suspend operator fun invoke(sleepId: String) = sleepRepository.deleteSleep(sleepId)
}