package dev.sobhy.babycareassistant.sleep.domain.usecases

import dev.sobhy.babycareassistant.sleep.data.repository.SleepRepository

class GetSleepByIdUseCase(
    private val sleepRepository: SleepRepository
) {
    suspend operator fun invoke(id: String) = sleepRepository.getSleepById(id)
}