package com.awesomepdf.usecase

import com.awesomepdf.domain.model.ai.ChatAnswer
import com.awesomepdf.domain.model.ai.ChatMessage
import com.awesomepdf.domain.model.ai.NotesResult
import com.awesomepdf.domain.repository.AiRepository
import com.awesomepdf.domain.usecase.SummarizePdfUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SummarizePdfUseCaseTest {

    @Test
    fun `returns summary from repository`() = runTest {
        val repository = object : AiRepository {
            override suspend fun summarize(text: String): String = "summary:$text"
            override suspend fun makeNotes(text: String): NotesResult = NotesResult(emptyList())
            override suspend fun chat(question: String, history: List<ChatMessage>): ChatAnswer = ChatAnswer("", emptyList())
        }

        val result = SummarizePdfUseCase(repository)("file content")

        assertThat(result).isEqualTo("summary:file content")
    }
}
