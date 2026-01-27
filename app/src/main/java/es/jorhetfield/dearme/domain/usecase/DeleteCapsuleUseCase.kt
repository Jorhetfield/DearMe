package es.jorhetfield.dearme.domain.usecase

import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import javax.inject.Inject

class DeleteCapsuleUseCase @Inject constructor(
    private val repository: CapsuleRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteCapsule(id)
    }
}
