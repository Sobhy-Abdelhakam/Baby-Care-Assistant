package dev.sobhy.babycareassistant.sleep.domain.usecases

import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository
import dev.sobhy.babycareassistant.growth.data.repository.GrowthRepository
import dev.sobhy.babycareassistant.sleep.data.repository.SleepRepository

class DeleteSleepUseCase(
    private val sleepRepository: SleepRepository
) {
    suspend operator fun invoke(sleepId: String) = sleepRepository.deleteSleep(sleepId)
}