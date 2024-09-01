package dev.sobhy.babycareassistant.vaccination.domain.usecases

import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import dev.sobhy.babycareassistant.vaccination.data.repository.VaccinationRepository

class GetVaccinationsUseCase(
    private val vaccinationRepository: VaccinationRepository
) {
    suspend operator fun invoke() = vaccinationRepository.getVaccinations()

}