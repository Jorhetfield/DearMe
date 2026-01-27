package es.jorhetfield.dearme.domain.usecase

import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import javax.inject.Inject

class GetCapsuleByIdUseCase @Inject constructor(
    private val repository: CapsuleRepository
) {
    suspend operator fun invoke(id: String): Capsule? {
        return repository.getCapsuleById(id)
    }
}
