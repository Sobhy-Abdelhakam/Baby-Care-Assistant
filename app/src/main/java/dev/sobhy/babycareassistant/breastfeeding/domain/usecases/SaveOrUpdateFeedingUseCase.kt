package dev.sobhy.babycareassistant.breastfeeding.domain.usecases

import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository

class SaveOrUpdateFeedingUseCase(
    private val feedingRepository: FeedingRepository
) {
    operator fun invoke(breastFeed: BreastFeed) = feedingRepository.saveOrUpdateBreastFeed(breastFeed)
}