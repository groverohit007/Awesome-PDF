package com.awesomepdf.data.ai

import com.awesomepdf.domain.repository.AiRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepositoryImpl @Inject constructor() : AiRepository {
    override suspend fun summarize(text: String): String {
        delay(400)
        return "Stub summary from backend proxy: ${text.take(100)}..."
    }

    override suspend fun chat(prompt: String): String {
        delay(300)
        return "Stub AI answer for: $prompt"
    }
}
