package dev.sobhy.babycareassistant.diapers.domain.usecase

import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.diapers.data.repository.DiapersRepository

class SaveOrUpdateDiaperUseCase(
    private val diapersRepository: DiapersRepository
) {
    operator fun invoke(diapers: Diapers) = diapersRepository.saveOrUpdateDiapers(diapers)
}