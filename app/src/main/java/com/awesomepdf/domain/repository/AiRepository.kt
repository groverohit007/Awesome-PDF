package com.awesomepdf.domain.repository

import com.awesomepdf.domain.model.ai.ChatAnswer
import com.awesomepdf.domain.model.ai.ChatMessage
import com.awesomepdf.domain.model.ai.NotesResult

interface AiRepository {
    suspend fun summarize(text: String): String
    suspend fun makeNotes(text: String): NotesResult
    suspend fun chat(question: String, history: List<ChatMessage>): ChatAnswer
}
