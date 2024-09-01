package dev.sobhy.babycareassistant.diapers.domain.usecase

import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository
import dev.sobhy.babycareassistant.diapers.data.repository.DiapersRepository

class GetDiapersUseCase(
    private val diapersRepository: DiapersRepository
) {
    suspend operator fun invoke() = diapersRepository.getDiapers()
}