package com.awesomepdf.domain.usecase

import com.awesomepdf.domain.repository.AiRepository
import javax.inject.Inject

class SummarizePdfUseCase @Inject constructor(
    private val aiRepository: AiRepository
) {
    suspend operator fun invoke(content: String): String {
        require(content.isNotBlank()) { "Content cannot be blank" }
        return aiRepository.summarize(content)
    }
}
