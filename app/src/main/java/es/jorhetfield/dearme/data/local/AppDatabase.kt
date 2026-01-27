package es.jorhetfield.dearme.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import es.jorhetfield.dearme.data.local.dao.CapsuleDao
import es.jorhetfield.dearme.data.local.entity.CapsuleEntity

@Database(
    entities = [CapsuleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun capsuleDao(): CapsuleDao
}
