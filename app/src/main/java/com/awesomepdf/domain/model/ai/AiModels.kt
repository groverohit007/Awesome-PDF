package com.awesomepdf.domain.model.ai

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatAnswer(
    val answer: String,
    val citations: List<String>
)

data class NotesResult(
    val bullets: List<String>
)
