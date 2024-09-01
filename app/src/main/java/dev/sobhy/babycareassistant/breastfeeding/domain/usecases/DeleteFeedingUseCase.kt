package dev.sobhy.babycareassistant.breastfeeding.domain.usecases

import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository

class DeleteFeedingUseCase(
    private val feedingRepository: FeedingRepository
) {
    suspend operator fun invoke(feedingId: String) = feedingRepository.deleteBreastFeed(feedingId)
}