package es.jorhetfield.dearme.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.jorhetfield.dearme.data.repository.CapsuleRepositoryImpl
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCapsuleRepository(
        capsuleRepositoryImpl: CapsuleRepositoryImpl
    ): CapsuleRepository
}
