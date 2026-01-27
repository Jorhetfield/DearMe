package es.jorhetfield.dearme.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.jorhetfield.dearme.data.local.entity.CapsuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CapsuleDao {

    @Query("SELECT * FROM capsules ORDER BY creationDate DESC")
    fun getAllCapsules(): Flow<List<CapsuleEntity>>

    @Query("SELECT * FROM capsules WHERE id = :id")
    suspend fun getCapsuleById(id: String): CapsuleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCapsule(capsule: CapsuleEntity)

    @Update
    suspend fun updateCapsule(capsule: CapsuleEntity)

    @Query("DELETE FROM capsules WHERE id = :id")
    suspend fun deleteCapsule(id: String)
}
