package dev.sobhy.babycareassistant.diapers.domain.usecase

import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository
import dev.sobhy.babycareassistant.diapers.data.repository.DiapersRepository

class GetDiaperByIdUseCase(
    private val diapersRepository: DiapersRepository
) {
    suspend operator fun invoke(id: String) = diapersRepository.getDiapersById(id)
}