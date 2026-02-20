package es.jorhetfield.dearme.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.jorhetfield.dearme.data.local.AppDatabase
import es.jorhetfield.dearme.data.local.dao.CapsuleDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "dearme_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCapsuleDao(db: AppDatabase): CapsuleDao {
        return db.capsuleDao()
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}
