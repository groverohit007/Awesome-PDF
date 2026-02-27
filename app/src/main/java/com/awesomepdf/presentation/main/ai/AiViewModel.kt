package com.awesomepdf.presentation.main.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomepdf.domain.model.ai.ChatMessage
import com.awesomepdf.domain.repository.AiRepository
import com.awesomepdf.domain.usecase.SummarizePdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UsageMeter(
    val used: Int = 0,
    val monthlyLimit: Int = 30
) {
    val remaining: Int get() = (monthlyLimit - used).coerceAtLeast(0)
}

@HiltViewModel
class AiViewModel @Inject constructor(
    private val summarizePdfUseCase: SummarizePdfUseCase,
    private val aiRepository: AiRepository
) : ViewModel() {

    private val _summary = MutableStateFlow("Upload/select a PDF to summarize")
    val summary = _summary.asStateFlow()

    private val _notes = MutableStateFlow<List<String>>(emptyList())
    val notes = _notes.asStateFlow()

    private val _chat = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chat = _chat.asStateFlow()

    private val _citations = MutableStateFlow<List<String>>(emptyList())
    val citations = _citations.asStateFlow()

    private val _usage = MutableStateFlow(UsageMeter())
    val usage = _usage.asStateFlow()

    private fun consumeQuota() {
        _usage.value = _usage.value.copy(used = (_usage.value.used + 1).coerceAtMost(_usage.value.monthlyLimit))
    }

    private fun hasQuota(): Boolean = _usage.value.remaining > 0

    fun summarizeExtractedText(extractedText: String) {
        if (!hasQuota()) {
            _summary.value = "Monthly AI quota exceeded"
            return
        }
        viewModelScope.launch {
            _summary.value = summarizePdfUseCase(extractedText)
            consumeQuota()
        }
    }

    fun makeNotes(extractedText: String) {
        if (!hasQuota()) {
            _notes.value = listOf("Monthly AI quota exceeded")
            return
        }
        viewModelScope.launch {
            _notes.value = aiRepository.makeNotes(extractedText).bullets
            consumeQuota()
        }
    }

    fun askPdf(question: String) {
        if (!hasQuota()) {
            _chat.value = _chat.value + ChatMessage("assistant", "Monthly AI quota exceeded")
            return
        }
        if (question.isBlank()) return
        viewModelScope.launch {
            val current = _chat.value
            _chat.value = current + ChatMessage("user", question)
            val response = aiRepository.chat(question, current)
            _chat.value = _chat.value + ChatMessage("assistant", response.answer)
            _citations.value = response.citations
            consumeQuota()
        }
    }

    fun exportNotesMarkdown(): String {
        val lines = _notes.value.joinToString("\n") { "- $it" }
        return "# PDF Notes\n\n$lines\n"
    }
}
