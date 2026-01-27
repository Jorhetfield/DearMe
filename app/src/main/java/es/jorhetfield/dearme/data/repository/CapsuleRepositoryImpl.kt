package es.jorhetfield.dearme.data.repository

import es.jorhetfield.dearme.data.local.dao.CapsuleDao
import es.jorhetfield.dearme.data.local.entity.toEntity
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CapsuleRepositoryImpl @Inject constructor(
    private val capsuleDao: CapsuleDao
) : CapsuleRepository {

    override fun getAllCapsules(): Flow<List<Capsule>> {
        return capsuleDao.getAllCapsules()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getCapsuleById(id: String): Capsule? {
        return capsuleDao.getCapsuleById(id)?.toDomain()
    }

    override suspend fun insertCapsule(capsule: Capsule) {
        capsuleDao.insertCapsule(capsule.toEntity())
    }

    override suspend fun updateCapsule(capsule: Capsule) {
        capsuleDao.updateCapsule(capsule.toEntity())
    }

    override suspend fun deleteCapsule(id: String) {
        capsuleDao.deleteCapsule(id)
    }
}
