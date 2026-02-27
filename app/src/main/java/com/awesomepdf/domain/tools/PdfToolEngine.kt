package com.awesomepdf.domain.tools

interface PdfToolEngine {
    suspend fun mergePdf(
        inputUris: List<String>,
        outputFileName: String,
        onProgress: (Int) -> Unit = {}
    ): String
}
