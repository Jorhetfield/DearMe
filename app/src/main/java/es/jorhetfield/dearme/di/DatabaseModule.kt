package es.jorhetfield.dearme.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.jorhetfield.dearme.data.local.AppDatabase
import es.jorhetfield.dearme.data.local.dao.CapsuleDao
import es.jorhetfield.dearme.ui.screens.addcapsule.AudioRecorderManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app

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

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideAudioRecorderManager(context: Context): AudioRecorderManager = AudioRecorderManager(context)
}
