package com.awesomepdf.data.auth

import com.awesomepdf.domain.model.UserProfile
import com.awesomepdf.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override val user: Flow<UserProfile?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val current = auth.currentUser
            trySend(current?.let { UserProfile(it.uid, it.displayName, it.email) })
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override suspend fun signInAnonymously(): Result<Unit> = runCatching {
        val user = firebaseAuth.signInAnonymously().await().user ?: return@runCatching
        saveProfile(user.uid, user.displayName, user.email)
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val user = firebaseAuth.signInWithCredential(credential).await().user ?: return@runCatching
        saveProfile(user.uid, user.displayName, user.email)
    }

    override suspend fun skipSignIn(): Result<Unit> = signInAnonymously()

    override suspend fun signOut(): Result<Unit> = runCatching {
        firebaseAuth.signOut()
    }

    override suspend fun syncUserSettings(settings: Map<String, Any?>): Result<Unit> = runCatching {
        val uid = firebaseAuth.currentUser?.uid ?: return@runCatching
        firestore.collection("profiles").document(uid)
            .set(settings + mapOf("updatedAt" to System.currentTimeMillis()), com.google.firebase.firestore.SetOptions.merge())
            .await()
    }

    private suspend fun saveProfile(uid: String, name: String?, email: String?) {
        firestore.collection("profiles").document(uid)
            .set(
                mapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "updatedAt" to System.currentTimeMillis()
                ),
                com.google.firebase.firestore.SetOptions.merge()
            )
            .await()
    }
}
