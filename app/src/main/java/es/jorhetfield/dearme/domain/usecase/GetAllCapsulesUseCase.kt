package es.jorhetfield.dearme.domain.usecase

import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCapsulesUseCase @Inject constructor(
    private val repository: CapsuleRepository
) {
    operator fun invoke(): Flow<List<Capsule>> {
        return repository.getAllCapsules()
    }
}
