package es.jorhetfield.dearme.domain.usecase

import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import javax.inject.Inject

class InsertCapsuleUseCase @Inject constructor(
    private val repository: CapsuleRepository
) {
    suspend operator fun invoke(capsule: Capsule) {
        repository.insertCapsule(capsule)
    }
}
