package com.awesomepdf.presentation.main.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomepdf.domain.usecase.SummarizePdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AiViewModel @Inject constructor(
    private val summarizePdfUseCase: SummarizePdfUseCase
) : ViewModel() {
    private val _response = MutableStateFlow("Ask AI about your PDF")
    val response = _response.asStateFlow()
    private val _quota = MutableStateFlow(5)
    val quota = _quota.asStateFlow()

    fun summarize(input: String) {
        if (_quota.value <= 0) {
            _response.value = "Quota exceeded"
            return
        }
        viewModelScope.launch {
            _response.value = summarizePdfUseCase(input)
            _quota.value -= 1
        }
    }
}
