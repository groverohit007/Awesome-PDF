package com.awesomepdf.domain.repository

import com.awesomepdf.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val user: Flow<UserProfile?>
    suspend fun signInAnonymously(): Result<Unit>
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    suspend fun skipSignIn(): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun syncUserSettings(settings: Map<String, Any?>): Result<Unit>
}
