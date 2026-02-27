package com.awesomepdf.data.ai

import com.awesomepdf.data.ai.network.AiApiService
import com.awesomepdf.data.ai.network.ChatMessageDto
import com.awesomepdf.data.ai.network.ChatRequest
import com.awesomepdf.data.ai.network.NotesRequest
import com.awesomepdf.data.ai.network.SummarizeRequest
import com.awesomepdf.domain.model.ai.ChatAnswer
import com.awesomepdf.domain.model.ai.ChatMessage
import com.awesomepdf.domain.model.ai.NotesResult
import com.awesomepdf.domain.repository.AiRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

@Singleton
class AiRepositoryImpl @Inject constructor(
    private val api: AiApiService
) : AiRepository {

    override suspend fun summarize(text: String): String {
        return try {
            api.summarize(SummarizeRequest(text)).summary
        } catch (_: Throwable) {
            delay(350)
            "Stub summary from backend proxy: ${text.take(180)}..."
        }
    }

    override suspend fun makeNotes(text: String): NotesResult {
        return try {
            val response = api.notes(NotesRequest(text))
            NotesResult(response.bullets)
        } catch (_: Throwable) {
            delay(250)
            NotesResult(
                listOf(
                    "Key idea: ${text.take(60)}",
                    "Important point: extract and review figures/tables.",
                    "Action: verify references and definitions."
                )
            )
        }
    }

    override suspend fun chat(question: String, history: List<ChatMessage>): ChatAnswer {
        return try {
            val response = api.chat(
                ChatRequest(
                    question = question,
                    history = history.map { ChatMessageDto(it.role, it.content) }
                )
            )
            ChatAnswer(response.answer, response.citations)
        } catch (_: Throwable) {
            delay(220)
            ChatAnswer(
                answer = "Stub answer: Based on uploaded PDF context, $question",
                citations = listOf("p.2 §1", "p.5 bullet 3")
            )
        }
    }
}
