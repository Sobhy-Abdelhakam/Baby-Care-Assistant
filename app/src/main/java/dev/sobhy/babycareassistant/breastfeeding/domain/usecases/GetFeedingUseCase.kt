package dev.sobhy.babycareassistant.breastfeeding.domain.usecases

import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository

class GetFeedingUseCase(
    private val feedingRepository: FeedingRepository
) {
    suspend operator fun invoke() = feedingRepository.getBreastFeed()
}