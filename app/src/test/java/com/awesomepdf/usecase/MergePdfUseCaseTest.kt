package com.awesomepdf.usecase

import com.awesomepdf.domain.tools.PdfToolEngine
import com.awesomepdf.domain.usecase.MergePdfUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MergePdfUseCaseTest {

    @Test
    fun `delegates merge to engine`() = runTest {
        val engine = object : PdfToolEngine {
            override suspend fun mergePdf(
                inputUris: List<String>,
                outputFileName: String,
                onProgress: (Int) -> Unit
            ): String {
                onProgress(100)
                return "/tmp/$outputFileName"
            }
        }

        val result = MergePdfUseCase(engine)(
            inputUris = listOf("content://a.pdf", "content://b.pdf"),
            outputFileName = "merged.pdf"
        )

        assertThat(result).isEqualTo("/tmp/merged.pdf")
    }
}
