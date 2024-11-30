package dev.sobhy.babycareassistant.diapers.domain.usecase

import dev.sobhy.babycareassistant.diapers.data.repository.DiapersRepository

class DeleteDiaperUseCase(
    private val diaperRepository: DiapersRepository
) {
    suspend operator fun invoke(diaperId: String) = diaperRepository.deleteDiapers(diaperId)
}