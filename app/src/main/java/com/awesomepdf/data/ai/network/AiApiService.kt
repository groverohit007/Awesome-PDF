package com.awesomepdf.data.ai.network

import retrofit2.http.Body
import retrofit2.http.POST

data class SummarizeRequest(val text: String)
data class SummarizeResponse(val summary: String)

data class NotesRequest(val text: String)
data class NotesResponse(val bullets: List<String>)

data class ChatRequest(val question: String, val history: List<ChatMessageDto>)
data class ChatMessageDto(val role: String, val content: String)
data class ChatResponse(val answer: String, val citations: List<String>)

interface AiApiService {
    @POST("v1/ai/summarize")
    suspend fun summarize(@Body body: SummarizeRequest): SummarizeResponse

    @POST("v1/ai/notes")
    suspend fun notes(@Body body: NotesRequest): NotesResponse

    @POST("v1/ai/chat")
    suspend fun chat(@Body body: ChatRequest): ChatResponse
}
