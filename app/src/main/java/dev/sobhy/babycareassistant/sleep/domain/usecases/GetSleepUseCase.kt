package dev.sobhy.babycareassistant.sleep.domain.usecases

import dev.sobhy.babycareassistant.sleep.data.repository.SleepRepository

class GetSleepUseCase(
    private val sleepRepository: SleepRepository
) {
    suspend operator fun invoke() = sleepRepository.getSleeps()
}