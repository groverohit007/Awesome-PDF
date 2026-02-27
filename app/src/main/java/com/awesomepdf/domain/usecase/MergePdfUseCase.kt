package com.awesomepdf.domain.usecase

import com.awesomepdf.domain.tools.PdfToolEngine
import javax.inject.Inject

class MergePdfUseCase @Inject constructor(
    private val pdfToolEngine: PdfToolEngine
) {
    suspend operator fun invoke(
        inputUris: List<String>,
        outputFileName: String,
        onProgress: (Int) -> Unit = {}
    ): String {
        require(inputUris.size >= 2) { "Select at least 2 PDFs" }
        require(outputFileName.isNotBlank()) { "Output file name cannot be blank" }
        return pdfToolEngine.mergePdf(inputUris, outputFileName, onProgress)
    }
}
