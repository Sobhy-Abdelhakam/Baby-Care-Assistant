package dev.sobhy.babycareassistant.breastfeeding.domain.usecases

import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository

class GetFeedingByIdUseCase(
    private val feedingRepository: FeedingRepository
) {
    suspend operator fun invoke(id: String) = feedingRepository.getBreastFeedById(id)
}