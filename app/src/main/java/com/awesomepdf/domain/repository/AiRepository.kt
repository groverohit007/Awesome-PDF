package com.awesomepdf.domain.repository

interface AiRepository {
    suspend fun summarize(text: String): String
    suspend fun chat(prompt: String): String
}
