package com.awesomepdf.domain.repository

import com.awesomepdf.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val user: Flow<UserProfile?>
    suspend fun signInAnonymously(): Result<Unit>
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    suspend fun skipSignIn(): Result<Unit>
}
