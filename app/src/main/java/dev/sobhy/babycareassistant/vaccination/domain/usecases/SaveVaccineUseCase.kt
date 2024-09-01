package dev.sobhy.babycareassistant.vaccination.domain.usecases

import com.google.android.gms.tasks.Task
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import dev.sobhy.babycareassistant.vaccination.data.repository.VaccinationRepository

class SaveVaccineUseCase(
    private val vaccineRepository: VaccinationRepository
) {
    operator fun invoke(vaccination: Vaccination): Task<Void> {
        return vaccineRepository.saveVaccination(vaccination)
    }
}