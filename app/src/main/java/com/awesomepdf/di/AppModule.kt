package com.awesomepdf.di

import com.awesomepdf.data.ai.AiRepositoryImpl
import com.awesomepdf.data.auth.AuthRepositoryImpl
import com.awesomepdf.data.billing.BillingRepositoryImpl
import com.awesomepdf.domain.repository.AiRepository
import com.awesomepdf.domain.repository.AuthRepository
import com.awesomepdf.domain.repository.BillingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindBillingRepository(impl: BillingRepositoryImpl): BillingRepository

    @Binds
    abstract fun bindAiRepository(impl: AiRepositoryImpl): AiRepository

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}
